# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Data Layer Rules

This layer implements domain interfaces. It may import Android/Firebase/Retrofit/Room, but must never import `presentation/` classes. Domain types (`domain/entity/`, `domain/exception/`) flow inward — this layer depends on them, not the other way.

### Package Structure

```
data/
├── mapper/         8 mapper objects + 1 exception mapper (all are object singletons)
├── network/
│   ├── api/        Retrofit: ApiService, response DTOs, param enums, constants
│   └── firebase/   Auth + Firestore sources (interfaces + impls), request/response DTOs
├── local/
│   ├── database/   Room: ChapterCacheDatabase, ChapterCacheDao, ChapterCacheEntity
│   └── prefs/      DataStore: ThemePrefsManager
└── repository/
    ├── manga/      MangaRepositoryImpl, ChapterRepositoryImpl, CacheRepositoryImpl
    ├── category/   CategoryRepositoryImpl
    ├── settings/   SettingsRepositoryImpl
    └── user/       UserRepositoryImpl, FavoritesRepositoryImpl, HistoryRepositoryImpl
```

---

## Exception Mapping

All exception mapping is centralised in `data/mapper/ExceptionMapper.kt`. Choose the correct function based on call site:

| Function | Use for |
|---|---|
| `Exception.toDomainException()` | Retrofit call catch blocks (HTTP + IO) |
| `Throwable.toUnexpectedException()` | Room / generic suspend catch blocks; Auth Flow `.catch {}` |
| `Exception.toFirebaseFirestoreException()` | Firestore **suspend** write/delete operations |
| `Throwable.toFirebaseFirestoreFlowException()` | Firestore **Flow** `.catch {}` operators |
| `Exception.toFirebaseAuthException()` | Firebase Auth suspend operations |

**Mapping rules:**

`toDomainException()`:
- `HttpException >= 500` → `InfrastructureException.ServerUnavailable`
- `HttpException < 500` → `InfrastructureException.Unexpected`
- `IOException` → `InfrastructureException.NetworkUnavailable`

`toFirebaseFirestoreException()` / `toFirebaseFirestoreFlowException()`:
- `PERMISSION_DENIED` → `BusinessException.Resource.AccessDenied`
- `UNAVAILABLE` / `DEADLINE_EXCEEDED` (flow only) → `InfrastructureException.NetworkUnavailable`

`toFirebaseAuthException()`:
- `FirebaseAuthUserCollisionException` → `BusinessException.Auth.UserAlreadyExists`
- `FirebaseAuthInvalidUserException` → `BusinessException.Auth.UserNotFound`
- `FirebaseAuthInvalidCredentialsException` → `BusinessException.Auth.InvalidCredentials`

All mapper functions rethrow `DomainException` and `CancellationException` unchanged — never catch and re-wrap them.

---

## Repository Pattern

Every repository method follows this template:

```kotlin
// Suspend (one-shot)
override suspend fun getSomething(): SomeType =
    AsyncHandler.runSuspendCatching(
        context = Dispatchers.IO,
        block = { apiService.getSomething().toSomeDomain() },
        catch = { it.toDomainException() }          // choose the right mapper
    )

// Flow (reactive) — Firestore
override fun observeSomething(): Flow<List<T>> =
    firestoreSource.observeSomething()
        .map { items -> items.map { it.toDomain() } }
        .catch { e -> e.toFirebaseFirestoreFlowException() }
        .flowOn(Dispatchers.IO)
        .distinctUntilChanged()
```

`CancellationException` must be rethrown **before** the catch block in any try/catch-based code. See `UserRepositoryImpl.register()` for the canonical example with best-effort rollback:

```kotlin
try {
    // ... operation
} catch (c: CancellationException) {
    throw c
} catch (e: Exception) {
    e.toFirebaseAuthException()
}
```

---

## Mapper Objects

All mappers are `object` — call as qualified static imports, never instantiate.

### `ApiParamMapper`

Converts between domain enums and API strings. **ISO codes and API string values live here only** — not in domain enums.

- `MangaLanguage.toApiParam()`, `MangaStatus.toApiParam()`, etc. → API query string
- `String?.toMangaLanguage()`, `String?.toMangaStatus()`, etc. → domain enum (all default to `UNKNOWN`)
- Matching is by enum name via `entries.find { it.name == domainEnum.name }` — domain enum names and param enum names are identical.

### `MangaMapper`

Cover URL construction: `{UPLOAD_URL}/covers/{mangaId}/{fileName}` — `UPLOAD_URL` comes from `BuildConfig.UPLOAD_URL` (injected from `local.properties`). The mapper receives `uploadUrl: String` as a parameter.

Relationship parsing: scans `relationships` list for `type == "cover_art"` / `"author"` / `"artist"`.

### `ChapterPagesMapper`

Page URL construction: `{baseUrl}/data/{dataHash}/{pageHash}` for each hash in `chapter.data`.

`ChapterPages → ChapterCacheEntity`: extracts page hashes by splitting stored URLs on `/`.
`ChapterCacheEntity → ChapterPages`: reconstructs full URLs from cached hashes.

### `CategoryMapper`

Uses `attributes.group` string to map to `CategoryType` via case-insensitive `entries.firstOrNull` — can handle any casing from the API.

### `UserMapper`

The only bidirectional mapper: `FirebaseUser.toUser()`, `UserProfileResponse.toUser()`, and `User.toUserProfileRequest()`. The `avatarUrl ↔ profilePictureUrl` rename happens here.

### `ReadingHistoryMapper`

The `pageCount ↔ totalChapterPages` rename happens here (domain name vs Firebase field name).

---

## Retrofit API

**`ApiService`** — all methods are `suspend fun` returning response wrapper types.

Endpoint constants are in `ApiEndpoints.kt`, query param names in `ApiQueries.kt`, path param names in `ApiPaths.kt`. Always use these constants for `@GET`, `@Path`, and `@Query` annotations — no raw strings.

Default `includes[]` parameters per endpoint type:
- Manga list/details: `[cover_art, author, artist]`
- Chapter list: `[scanlation_group]`
- Chapter details: `[manga, scanlation_group]`

Sort criteria mapping in `CategoryRepositoryImpl.getMangaListByCategory()` — `MangaSortCriteria` maps to different `@Query` params:
- `LATEST_UPDATE` → `ORDER_UPDATED_AT`
- `TRENDING` → `ORDER_FOLLOWED_COUNT`
- `NEW_RELEASE` → `ORDER_CREATED_AT`
- `TOP_RATED` → `ORDER_RATING`

`UNKNOWN` values in status/contentRating filter lists are stripped before calling the API.

**`IsoDateTimeAdapter`** — Moshi adapter for `Long? ↔ ISO8601 String?`. Uses `ThreadLocal<SimpleDateFormat>` for thread safety. Registered in `ApiModule` alongside `KotlinJsonAdapterFactory`.

---

## Firebase DTOs

### Request DTOs (writes)

- `id` field is annotated `@get:Exclude` — it becomes the Firestore **document ID**, not a stored field.
- All other fields use `@get:PropertyName("snake_case")` — Firestore stores snake_case keys.
- `createdAt` / `updatedAt` use `@ServerTimestamp val field: Date? = null` — Firebase server sets the value automatically.

### Response DTOs (reads)

- All fields are `var` (mutable) — required for Firestore's reflection-based deserialization.
- `id: String = ""` is `@get:Exclude @set:Exclude` and populated manually from `document.id` after deserialization.
- Pattern: `doc.toObject(FooResponse::class.java)?.copy(id = doc.id)`

### Firestore Collection Paths

```
/users/{userId}                      — user profile document
/users/{userId}/favorites/{mangaId}  — favorite entry (document ID = manga ID)
/users/{userId}/history/{historyId}  — history entry (document ID from ReadingHistory.generateId())
```

Constants: `FirestoreCollections.USERS`, `.FAVORITES`, `.HISTORY` / `FirestoreFields.CREATED_AT`, `.MANGA_ID`.

### Firestore Pagination

All paginated Firestore queries use **cursor-based pagination** with `startAfter(lastDocument)`:
1. First page: ordered query with `.limit(n)`
2. Next pages: fetch the last document snapshot, then `.startAfter(lastDoc).limit(n)`

The `lastFavoriteMangaId` / `lastReadingHistoryId` params trigger this — `null` means first page.

---

## Room Database

**`ChapterCacheEntity`** — one column name does not match its Kotlin property name:
- Property: `dataHash: String`
- Column: `@ColumnInfo(name = "chapterDataHash")` — kept for DB schema compatibility (do not rename the column)

Room DB version: `2` with `fallbackToDestructiveMigration()` (no manual migrations).

`StringListTypeConverter` serializes `List<String>` to/from JSON using kotlinx.serialization. It is an `object` with `@JvmStatic @TypeConverter` methods.

Cache expiry is time-based: `DELETE WHERE cachedAt < expiryTimestamp`. The expiry threshold (24h) is computed in `ClearExpiredCacheUseCase`, not here.

---

## DataStore

`ThemePrefsManager` is a Kotlin `object` that declares a `Context.dataStore` extension property via `preferencesDataStore(name = "theme_prefs_manager")`. `LocalModule` provides the `DataStore<Preferences>` instance by calling `context.dataStore`.

`SettingsRepositoryImpl` stores the theme mode enum as its `.name` string and reads it back with `ThemeMode.valueOf(...)`, defaulting to `ThemeMode.SYSTEM`.
