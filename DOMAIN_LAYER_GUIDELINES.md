# Tiêu chuẩn Domain Layer - Clean Architecture (Uncle Bob)

Tài liệu này đóng vai trò là kim chỉ nam (guidelines) chuẩn hoá việc refactor/migrate Business Logic từ Presentation Layer (ViewModel) vào đúng vị trí trong **Domain Layer**, dựa trên hệ thống Clean Architecture cốt lõi của **Uncle Bob**.

---

## 1. Lý thuyết tổng quan (Kiến trúc chuẩn của Uncle Bob)

Domain Layer (Lớp Nghiệp vụ / Lõi) là vòng tròn trung tâm nhất trong mô hình Clean Architecture. Nguyên tắc sống còn của Layer này là:
**KHÔNG PHỤ THUỘC VÀO BẤT KỲ VÒNG TRÒN BÊN NGOÀI NÀO KHÁC** (Hoàn toàn độc lập với UI, Database, Network, API hay Framework cụ thể như Android SDK, Retrofit, Room).

Domain Layer chứa 2 thành phần chủ lực:

1. **Entities (Enterprise Business Rules - Các quy tắc cốt lõi của doanh nghiệp):**
   - Là các đối tượng trung tâm của ứng dụng.
   - Trong Kotlin, thiết kế dưới dạng data classes/sealed classes, chứa dữ liệu VÀ các logic quy tắc liên quan mật thiết tới bản thân thực thể đó (Validation, các hàm thao tác trạng thái).
   - **Cảnh báo:** Tránh để Entity rơi vào tình trạng **Anemic Model** (Mô hình thiếu máu - tức là các class Model chỉ là cấu trúc/khai báo data fields mà không có hành vi/logic nào).
2. **Use Cases (Application Business Rules - Các quy tắc của ứng dụng):**
   - Đại diện cho duy nhất MỘT luồng hành động/nghiệp vụ.
   - Gọi Entity để thực thi luật lệ xác nhận, sau đó giao tiếp với các kho lưu trữ (Repositories) thông qua Interface.

---

## 2. Guideline Tái Phân Bổ Business Logic (Quy tắc Refactor CODE)

### Quy tắc 1: ViewModels (Presentation Layer) CHỈ LÀ "NGƯỜI GIAO LIÊN"

- **Do:** Nhận Event từ UI -> Gọi hàm ở UseCase (Triggers UseCase) -> Hứng kết quả KQ (`Result` / `Flow`) -> Ánh xạ (thành UiState/UiError) -> Chuyển giao về lại cho giao diện UI vẽ.
- **Don't:** Tuyệt đối không viết IF/ELSE kiểm tra nghiệp vụ, format String, Regex sâu bên trong Viewmodel.

```kotlin
// ❌ VÍ DỤ SAI DỄ GẶP: ViewModel tự làm Business Logic
fun submit() {
    if (email.isEmpty()) {
        _uiState.value = Error("Email không được trống")
        return
    }
    useCase.login(email, password)
}
```

### Quy tắc 2: Domain Entity Model NÊN CHỨA "QUY TẮC NỘI TẠI" (RICH MODEL)

Bất kì tính toán, điều kiện kiểm tra nào thuộc về chính xác thuộc tính tự nhiên của ĐỐI TƯỢNG ĐÓ, hãy đưa logic đó vào trong Entity. Việc này tránh code bị phân tán và giúp tái sử dụng validation ở mọi nơi.

**⚠️ Lưu ý phân biệt giữa Static Utility và Rich Model thực sự:**
Việc để hàm validate trong `companion object` về bản chất giống như một **Domain Service (Static Utility)** để tận dụng khi chưa có instance cụ thể (ví dụ tại màn hình lúc người dùng đang gõ text, đang Register).
Một Model được gọi là **Rich Model thực thụ** khi nó có các hàm hành vi (behavior) thay đổi và validate các tham số nằm bên trong chính bản thân instance đó.

```kotlin
// ✅ VÍ DỤ CHUẨN: Entity linh hoạt giữa Rich Model và Validation Service
data class User(
    val id: String,
    val name: String,
    val email: String
) {
    // 1. RICH MODEL: Hàm Instance — Validate trạng thái của CHÍNH NÓ (Ex: trước khi Update Profile)
    fun validateForUpdate() {
        if (name.isBlank()) throw AuthException.Name.Empty()
    }

    companion object {
        private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

        // 2. STATIC VALIDATION (Domain Service): Dùng khi người dùng chưa đăng nhập, chưa sinh ra object
        fun validateEmail(email: String) {
            if (email.isBlank()) throw AuthException.Email.Empty()
            if (!EMAIL_REGEX.matches(email)) throw AuthException.Email.Invalid()
        }
    }
}
```

### Quy tắc 3: Use Cases LÀ "NHẠC TRƯỞNG CHỈ HUY" (ORCHESTRATOR)

Mỗi UseCase sẽ đại diện làm **"Kịch Bản - Workflow"** điều phối.

_Lưu ý: Không lạm dụng scope function `apply {}` khi gọi các hàm Static Utility. Việc này dẫn đến việc người đọc tưởng có 1 Instance đang được sửa đổi._

**A. UseCase dạng One-shot Operation (Trả về Result<T>):**
Phù hợp với các action Cập nhật, Thêm, Xóa, Gửi Đi (Mutations).

```kotlin
// ✅ VÍ DỤ CHUẨN: UseCase điều phối luồng One-shot
class RegisterUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(email: String, pass: String, name: String): Result<Unit> = runSuspendCatching {
        // Bước 1: Gọi Static Validation (Model Service)
        User.validateEmail(email)
        User.validatePassword(pass)
        User.validateName(name)

        // Bước 2: Gọi Repository (I/O) với đủ params của luồng Register
        repository.register(email, pass, name)

        // Bước 3: Hàm runSuspendCatching tự động wrap Exception thành Result.failure nếu có lỗi
    }
}
```

**B. UseCase dạng Reactive Stream (Trả về Flow<T>):**
Phù hợp với tác vụ Đọc (Queries), Observe trạng thái Real-time (DB updates, Socket, User Session).

```kotlin
// ✅ VÍ DỤ CHUẨN: UseCase kết nối với một Reactive Stream (Flow)
class ObserveUserProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {
    // Lưu ý: Function này trả về Flow, thường KHÔNG CẦN là suspend function.
    operator fun invoke(userId: String): Flow<Result<User?>> {
        return repository.observeUser(userId)
            .map { user ->
                Result.success(user) // Trả về dạng Result thành công
            }
            .catch { e ->
                emit(Result.failure(e)) // Flow tự động phát xạ Result lỗi nếu ném exception
            }
    }
}
```

### Quy tắc 4: Repository Interface CHỈ CHỨA "BẢN HỢP ĐỒNG" (CONTRACTS)

Dữ liệu đi vào và ra khỏi Repository (Interface) bắt buộc phải là Domain Entity/Domain Models. Việc Call API và Map Data là nhiệm vụ của Data Layer.

---

## 3. Khuyến nghị nâng cao (Advanced Principles)

**1. Dữ liệu băng qua Boundary (Repository trả về Model hay DTO?)**

- **Luôn là Domain Model!** Remote DTO (Data Transfer Object) từ JSON hay Database Entity (Room) phải được **giấu kín (ẩn) hoàn toàn ở Data Layer**. Hàm `.toDomain()` mapper phải được gọi ngay trước khi dữ liệu rời khỏi lớp Data để bàn giao cho Domain. Domain không bao giờ được biết cấu trúc của API trả về thế nào.

**2. Tuyệt đối không Leak Framework:**

- Domain phải hoàn toàn là Kotlin thuần.
- Tránh import các thư viện của Android (`android.*`) hoặc Data (`retrofit2.*`, `androidx.room.*`).
- **Không bao giờ** `@Entity`, `@SerializedName`, `@Inject` (đối với model) trong package `domain/model`.

**3. UseCase gọi UseCase khác - Nên hay không?:**

- **Hạn Chế Tối Đa:** Tạo thói quen UseCase gọi UseCase khác sẽ sinh ra "Hidden Dependencies" (Phụ thuộc ngầm) - cực kỳ khó Unit Test và dễ rớt vào vòng lặp (Circular Dependency).
- **Giải Pháp:** Nếu có logic dùng chung, hãy tách ra thành một file Domain Helper thuần (không phải UseCase), HOẶC gộp chúng lại tại ViewModel, HOẶC định nghĩa một "Facade UseCase" to hơn đi gọi các Repository riêng lẻ thay vì gọi UseCase con.

**4. UseCase có nên được Inject CoroutineDispatcher?**

- **Không cần thiết!** (Theo chuẩn Android/Google best practice).
- Tất cả các hàm thao tác I/O (`suspend` functions) từ tầng Data/Repository phải tự chịu trách nhiệm là "Main-safe". (Nghĩa là `Room Dao` hoặc `Retrofit` sẽ tự biết âm thầm đổi thread qua `Dispatchers.IO` bên dưới hood).
- UseCase chỉ việc gọi bình thường và không cần quan tâm Thread nào. **Chỉ inject Dispatcher (`Dispatchers.Default`) khi UseCase của bạn chứa một vòng lặp thuật toán tính toán Math/Logic cực nặng (CPU-bound) tự thân nó.**

---

## 4. Ranh giới xám (Gray Areas đáng lưu ý)

Trong thực tế thi công, một số yêu cầu Business Logic rất ranh giới, rất dễ gây cãi vã là nên để ở Domain hay hất ra ngoài. Sau đây là quy chuẩn:

1. **Pagination Logic (Logic Phân trang nằm ở UseCase hay Repository?):**
   - Sự vụ load `page = 1, pageSize = 20` bắt nguồn từ UI/ViewModel -> Truyền `Flow/PagingParams` qua UseCase -> Truyền xuống Repository.
   - Nhưng **Logic tính toán phân trang cache, PagingSource, offset, limit, loadMore** thực thụ thì TRĂM PHẦN TRĂM thuộc về **Data Layer** (Repository trả về hệ sinh thái Paging3 như `Flow<PagingData<T>>`).
   - **Chốt lại:** UseCase chỉ đóng vai trò Pass-through (chuyền bóng) Config Paging xuống. Không nhét vòng lặp hàm cộng trừ Index trang, config size List vào trong file UseCase.

2. **Sorting & Filtering (Sắp xếp / Lọc nhánh nằm ở đâu?):**
   - **Server-side Filtering (Lọc qua API backend):** Các bộ tham số Sort/Filter (vd: Các Enum class định nghĩa `SortOrder.DESC`, `FilterMode.ACTIVE`) phải nằm ở **Domain Layer**, truyền xuống UseCase rồi Repository báo Server xử lý.
   - **Client-side Filtering (Lọc cục bộ In-Memory List):** Nếu Repository đã trả lên 1 cục List siêu bự cho RAM, và cần thay đổi filter liên tục, logic filter/sort `list.filter { it.isActive }` này nên được thực thi tại **UseCase** trước khi trả kết quả cuối về cho Model. Việc này giúp test nhanh và giữ ViewModel không bị loãng. (ViewModel khi nhận list về chỉ lo duy nhất việc đưa vào `LazyColumn` hoặc RecyclerView).
