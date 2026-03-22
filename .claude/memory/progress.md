# Session Progress

## Completed This Session (2026-03-22)

### Data Layer Strict Kotlin Review — 9 parallel agents, all fixes applied

#### Critical bug fixes (would crash at runtime)
- `IsoDateTimeMoshiAdapter.kt` — `java.time.ZonedDateTime` requires API 26; minSdk is 24. Fixed to `SimpleDateFormat` (API 21+). Was a silent `NoClassDefFoundError` on API 24/25.
- `NetworkInterceptor.kt` — `jakarta.inject.Inject` instead of `javax.inject.Inject`. Hilt targets `javax.inject`; `jakarta` import causes Hilt to not generate the factory → runtime DI crash.
- `MangaRepositoryImpl.kt` — same `jakarta.inject.Inject` bug → runtime DI crash.
- `ApiQueries.kt` — `ORDER_RATING = "order[rating]=desc"` embedded the value in the key name → garbage query param (`?order[rating]=desc=desc`). Top-rated sort was silently broken.
- `UserRepositoryImpl.register()` — `runCatching { firebaseAuthSource.logout() }` called a suspend function from a non-suspend stdlib lambda → compile error (depending on Kotlin version) or wrong behavior.

#### Data integrity fixes
- `ApiParamMapper.kt` — `MangaSortOrder.toApiParam()` and `MangaLanguage.toApiParam()` used `valueOf(name)` which throws `IllegalArgumentException` if enum names ever diverge between domain and param enums.
- `ApiParamMapper.toMangaStatus/ContentRating/Language()` — same `valueOf` crash risk on reverse mapping.
- `ChapterPagesMapper.toChapterCacheEntity()` — `substringAfterLast("/")` returns full string when no `/` present → corrupted page hashes in cache.
- `MangaAttributesResponse.title` — non-nullable with no default → `JsonDataException` crash if `title` field is ever absent from API response.
- `ChapterResponse.relationships` — `List<RelationshipResponse?>?` had nullable list elements the API never produces → defensive safe calls throughout mapper.

#### Correctness improvements
- `CategoryRepositoryImpl` — chained `if/else` for `sortCriteria` → exhaustive `when` block (compile-time coverage check)
- `SettingsRepositoryImpl` — `entries[index]` → `getOrElse { SYSTEM }` (prevents `IndexOutOfBoundsException` on corrupt preference)
- `FavoritesRepositoryImpl/HistoryRepositoryImpl` — `flowOn` was after `.catch`; moved before so upstream IO errors are caught correctly
- `UserRepositoryImpl.register()` — no rollback when Firestore write fails after Auth registration; fixed with Auth account deletion on failure
- `ApiService.translatedLanguages` — `String` → `List<String>` (Retrofit serializes list as repeated params, not a single string)
- `TagAttributesResponse/RelationshipAttributesResponse.biography` — `Map<String, String?>` → `Map<String, String>` (nullable map values were incorrect)

#### Design/architecture improvements
- `ExceptionMapper` — added `toFirestoreFlowException()` (Throwable for Flow `.catch`) and `toFirestoreException()` (Exception for `onCatch`) to centralize PERMISSION_DENIED mapping
- `ChapterCacheDatabase` — `context.applicationContext` + `fallbackToDestructiveMigration(true)`
- `StringListTypeConverter` — private Json instance; SerializationException caught → cache miss instead of crash
- `ThemePrefsManager.dataStore` — made `internal`
- `ApiEndpoints.MANGA/CHAPTER_ID` — removed leading slashes (Retrofit convention)
- `UserProfileResponse` — removed duplicate `@ServerTimestamp createdAt` field
- `Manga.DEFAULT_COVER_URL` — added companion constant; `MangaMapper` updated to use it

---

## Still To Do

No known outstanding issues in the data layer. Changes are uncommitted (user commits manually).

## Single Most Important Next Step

Ask the user what to work on next — data layer strict review is fully complete.
