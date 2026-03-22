# Current Task

## Task
Strict Kotlin review of the entire data layer — 9 parallel review agents ran, all fixes applied to main repo. User chose to commit manually.

## Status: CHANGES UNCOMMITTED — build is green, all fixes are in the working tree

All changes are staged/modified in the working directory. The user will commit them manually.

---

### What was completed this session

**Round 1 review (9 agents)** — all fixes applied to main repo:

#### Local layer
- `ChapterCacheDatabase.kt`: `context` → `context.applicationContext`; `fallbackToDestructiveMigration(false)` → `(true)`
- `StringListTypeConverter.kt`: private `Json` instance with `ignoreUnknownKeys = true`; `decodeFromString` wrapped in `try/catch(SerializationException)` — corrupt cache returns `emptyList()` instead of crashing
- `ThemePrefsManager.kt`: `val Context.dataStore` → `internal val`

#### Mapper layer
- `ApiParamMapper.kt`: `MangaSortOrder.toApiParam()` and `MangaLanguage.toApiParam()` changed from unsafe `valueOf(name).value` to safe `entries.find { it.name == this.name }?.value ?: fallback`; `toMangaStatus`, `toMangaContentRating`, `toMangaLanguage` all changed from `valueOf(it.name)` to `entries.find { it.name == param.name }`; `toMangaLanguage` receiver now uses `.lowercase()`
- `ChapterPagesMapper.kt`: `substringAfterLast("/")` → `substringAfterLast("/", missingDelimiterValue = "")` (prevents silent data corruption when URL has no `/`)
- `ChapterMapper.kt`: `it?.type` → `it.type` (unnecessary safe call removed after `ChapterResponse.relationships` type fix)
- `MangaMapper.kt`: `?: ""` → `?: Manga.DEFAULT_COVER_URL`
- `ExceptionMapper.kt`: added `toFirestoreFlowException(): Nothing` (Throwable extension for Flow `.catch` blocks) and `toFirestoreException(): Nothing` (Exception extension for `onCatch` blocks)

#### Domain entity
- `Manga.kt`: added `const val DEFAULT_COVER_URL = ""`

#### Network layer
- `NetworkInterceptor.kt`: `jakarta.inject.Inject` → `javax.inject.Inject` (was causing runtime Hilt DI crash); builder chain simplified to `chain.proceed(chain.request())`
- `ApiEndpoints.kt`: removed leading `/` from `MANGA` and `CHAPTER_ID` constants
- `ApiQueries.kt`: `ORDER_RATING = "order[rating]=desc"` → `"order[rating]"` (embedded `=desc` in key name produced garbage query params)
- `ApiService.kt`: `translatedLanguages: String` → `List<String>` (Retrofit needs a list for repeated query params)
- `IsoDateTimeMoshiAdapter.kt`: complete rewrite — replaced `java.time.ZonedDateTime` (API 26) with `SimpleDateFormat` (API 24 safe, matches minSdk); was causing `NoClassDefFoundError` on API 24/25
- `MangaAttributesResponse.kt`: `title: Map<String, String>` → `title: Map<String, String>? = null` (non-nullable would crash entire parse if field absent)
- `TagAttributesResponse.kt`: `Map<String, String?>?` → `Map<String, String>?` (nullable map values were incorrect)
- `RelationshipAttributesResponse.kt`: same nullable map value fix
- `ChapterResponse.kt`: `List<RelationshipResponse?>?` → `List<RelationshipResponse>?` (nullable list elements were incorrect)
- `UserProfileResponse.kt`: removed duplicate `@ServerTimestamp var createdAt` field

#### Repository layer
- `MangaRepositoryImpl.kt`: `jakarta.inject.Inject` → `javax.inject.Inject` (runtime DI crash fix)
- `ChapterRepositoryImpl.kt`: `translatedLanguages = language.toApiParam()` → `listOf(language.toApiParam())`
- `CategoryRepositoryImpl.kt`: chained `if/else` → exhaustive `when(sortCriteria)` block
- `SettingsRepositoryImpl.kt`: `ThemeMode.entries[index]` → `getOrElse { ThemeMode.SYSTEM }`
- `FavoritesRepositoryImpl.kt`: `flowOn` moved before `.catch`; `.catch { e -> e.toFirestoreFlowException() }`; `onCatch = { it.toFirestoreException() }` for suspend ops
- `HistoryRepositoryImpl.kt`: same flowOn + toFirestoreFlowException/toFirestoreException pattern
- `UserRepositoryImpl.kt`: rollback Auth account on Firestore write failure during `register()` with correct `try { logout() } catch (_: Exception) { }` pattern; added missing `FirebaseAuthInvalidCredentialsException` mapping; `observeUserProfile` gets `.catch { e -> e.toFirestoreFlowException() }`; dead `is BusinessException.Auth -> throw e` guards removed

---

### No pending work

User will commit manually. Next session should start fresh on whatever the user wants to work on next.
