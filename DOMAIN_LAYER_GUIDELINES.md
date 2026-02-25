# Tiêu chuẩn Domain Layer - Clean Architecture (Uncle Bob)

Tài liệu kim chỉ nam chuẩn hoá việc refactor Business Logic từ Presentation Layer (ViewModel) vào đúng vị trí trong **Domain Layer**.

---

## 1. Tổng quan

Domain Layer là vòng tròn trung tâm nhất trong Clean Architecture, **không phụ thuộc vào bất kỳ layer nào khác** (UI, Database, Network, Framework).

Bao gồm 2 thành phần chính:

- **Entities**: Đối tượng trung tâm, chứa dữ liệu và logic nghiệp vụ thuộc về bản thân đối tượng đó.
- **Use Cases**: Đại diện cho một luồng hành động duy nhất, điều phối Entities và Repositories.

---

## 2. Quy tắc phân tầng Logic

### Quy tắc 1: ViewModel — "Người giao liên"

**Do:** Nhận Event từ UI → Gọi UseCase → Map `Result` → Cập nhật UiState.

**Don't:** Tuyệt đối không viết if/else nghiệp vụ, Regex, format String phức tạp trong ViewModel.

```kotlin
// ❌ SAI: ViewModel tự làm Business Logic
fun submit() {
    if (email.isEmpty()) {
        _uiState.value = Error("Email không được trống")
        return
    }
    useCase.login(email, password)
}
```

---

### Quy tắc 2: Domain Entity — "Người giữ quy tắc"

**Phân biệt 2 dạng:**

- **Rich Model (Instance Method):** Logic validate trạng thái của **chính instance đó** — dùng khi object đã tồn tại (ví dụ: trước khi Update Profile).
- **Static Validation (Companion Object):** Logic validate input đầu vào khi **chưa có instance** — dùng khi Register/Login.

```kotlin
data class User(val id: String, val name: String, val email: String) {

    // RICH MODEL: validate trạng thái của chính instance (Update case)
    fun validateForUpdate() {
        if (name.isBlank()) throw AuthException.Name.Empty()
    }

    companion object {
        private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

        // STATIC VALIDATION: dùng khi chưa có object (Register/Login case)
        fun validateEmail(email: String) {
            if (email.isBlank()) throw AuthException.Email.Empty()
            if (!EMAIL_REGEX.matches(email)) throw AuthException.Email.Invalid()
        }

        fun validatePassword(password: String) {
            if (password.isBlank()) throw AuthException.Password.Empty()
            if (password.length < MIN_PASSWORD_LENGTH) throw AuthException.Password.Weak()
        }
    }
}
```

> ⚠️ Không dùng `User.apply { validateEmail(email) }` — cú pháp này dễ gây hiểu nhầm người đọc đang thao tác trên một instance. Hãy gọi tường minh: `User.validateEmail(email)`.

---

### Quy tắc 3: Use Cases — "Nhạc trưởng"

Mỗi UseCase đại diện cho **một kịch bản nghiệp vụ duy nhất**.

**A. One-shot Operation → trả về `Result<T>` (dùng `runSuspendResultCatching`):**

Phù hợp với Mutations — Tạo, Cập nhật, Xóa, Gửi.

```kotlin
class RegisterUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(
        email: String, password: String, confirmPassword: String, name: String
    ): Result<Unit> = runSuspendResultCatching {
        // Bước 1: Static Validation
        User.validateEmail(email)
        User.validatePassword(password)
        User.validateConfirmPassword(password, confirmPassword)
        User.validateName(name)
        // Bước 2: Gọi Repository với đầy đủ params
        repository.register(email, password, name)
    }
}
```

**B. Reactive Stream → trả về `Flow<Result<T>>` (dùng `toFlowResult()`):**

Phù hợp với Queries — Đọc, Observe trạng thái real-time.

```kotlin
class ObserveUserProfileUseCase @Inject constructor(private val repository: UserRepository) {
    operator fun invoke(userId: String): Flow<Result<User?>> =
        repository.observeUserProfile(userId).toFlowResult()
}
```

---

### Quy tắc 4: Repository Interface — "Bản hợp đồng"

- Interface thuần: chỉ khai báo signature, không có implementation.
- Input/Output bắt buộc là **Domain Models** — không bao giờ trả về DTO, Response, Entity của Room hay Retrofit.
- Data Layer implement interface này và tự lo việc mapping.

---

## 3. AsyncHandler — Cơ sở hạ tầng xử lý lỗi

Tất cả error handling trong project được chuẩn hoá qua `AsyncHandler`:

| Hàm                                               | Trả về            | Dùng khi                                           |
| ------------------------------------------------- | ----------------- | -------------------------------------------------- |
| `runSuspendResultCatching { }`                    | `Result<T>`       | UseCase (Domain Layer)                             |
| `runSuspendCatching(context, onExecute, onCatch)` | `T` directly      | Repository Impl (Data Layer) với exception mapping |
| `Flow<T>.toFlowResult()`                          | `Flow<Result<T>>` | UseCase observe real-time stream                   |

**Lý do luôn phải rethrow `CancellationException`:**

```kotlin
// ❌ NGUY HIỂM: nuốt CancellationException, coroutine trở thành "zombie"
try { doSomething() } catch (e: Throwable) { Result.failure(e) }

// ✅ ĐÚNG: tách riêng và rethrow
} catch (c: CancellationException) {
    throw c  // preserve cancellation signal → structured concurrency hoạt động đúng
} catch (e: Throwable) {
    Result.failure(e)
}
```

**Repository sử dụng `runSuspendCatching` để map Framework Exception → Domain Exception:**

```kotlin
// UserRepositoryImpl.kt (Data Layer)
override suspend fun login(email: String, password: String) = runSuspendCatching(
    context = Dispatchers.IO,
    onExecute = { firebaseAuthSource.login(email, password) },
    onCatch = { e ->
        when (e) {
            is FirebaseAuthInvalidUserException -> throw UserException.NotFound(cause = e)
            else -> throw e
        }
    }
)
```

---

## 4. Khuyến nghị nâng cao

**1. Không leak Framework vào Domain:**
Tuyệt đối không có `@Entity`, `@SerializedName`, `import android.*` trong `domain/model`.

**2. UseCase không gọi UseCase khác:**
Tạo ra "Hidden Dependencies" khó unit test. Thay vào đó, gọi nhiều Repository riêng lẻ từ một UseCase, hoặc tạo một helper function thuần.

**3. UseCase không cần inject `CoroutineDispatcher`:**
`Room` và `Retrofit` đã tự "main-safe". UseCase chỉ inject Dispatcher khi chứa thuật toán CPU-bound nặng.

**4. Null parameter xử lý đúng tầng:**
Khi param optional (ví dụ `newName: String?`), UseCase phải quyết định rõ null có nghĩa là gì — "giữ nguyên giá trị cũ" hay "bỏ qua trường đó" — thay vì để ViewModel tự xử lý.

```kotlin
// UpdateUserProfileUseCase
// null name = "chỉ update ảnh, giữ nguyên tên" → fallback về tên hiện tại
val nameToUpdate = newName?.trim() ?: return@runSuspendResultCatching
```

---

## 5. Ranh giới xám (Gray Areas)

| Tình huống                  | Quyết định                                                                                              |
| --------------------------- | ------------------------------------------------------------------------------------------------------- |
| **Pagination**              | Params (page, pageSize) đi qua UseCase; logic PagingSource/PagingData thuộc Data Layer                  |
| **Server-side Filter/Sort** | Định nghĩa enum/params filter ở Domain, truyền qua UseCase → Repository → API                           |
| **Client-side Filter/Sort** | Logic `list.filter { }` thực hiện ở UseCase trước khi trả về ViewModel                                  |
| **Exception mapping**       | Framework Exception (Firebase, Retrofit) được map tại Data Layer; Domain Exception được throw từ Domain |
