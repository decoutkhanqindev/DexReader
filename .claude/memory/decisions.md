## 2026-03-18

### Decision: Move Status/ContentRating UiModels from `criteria/filter/` to `manga/`

**What was decided:**
`MangaStatusUiModel` and `MangaContentRatingUiModel` (previously `MangaStatusFilterUiModel` /
`MangaContentRatingFilterUiModel`) were moved from `presentation/model/criteria/filter/` to
`presentation/model/manga/`.

**Reasoning:**
The `Filter` suffix and `criteria/filter/` location implied these types were query/filter-only
constructs. In practice, `MangaUiModel` uses them as display properties (`val status`,
`val contentRating`). They are manga attributes, not filter parameters. The domain layer had already
been cleaned up in a prior session (promoting `MangaStatus`/`MangaContentRating` to
`domain/model/manga/`), so the presentation layer needed to align.

**Alternatives considered:**

- Keep them in `criteria/filter/` with a note — rejected because it perpetuates the misleading name
  and location.
- Create a separate `presentation/model/manga/attributes/` sub-package — rejected as unnecessary
  nesting; `manga/` package is the natural home alongside `MangaUiModel`.

**Scope boundary:**
Sort criteria (`MangaSortCriteriaUiModel`, `MangaSortOrderUiModel`) were deliberately left untouched
in `criteria/sort/` — they genuinely describe query behavior, not manga attributes.

---

### Decision: Absorb status/rating mappers into `MangaUiMapper` instead of a new dedicated mapper

**What was decided:**
The 4 extension functions (`toMangaStatusUiModel`, `toMangaStatus`, `toMangaContentRatingUiModel`,
`toMangaContentRating`) were moved into the `MangaUiMapper` object.

**Reasoning:**
These mappers exist to support `Manga.toMangaUiModel()` and `FavoriteManga.toMangaUiModel()`.
Keeping them co-located with the manga mapping logic avoids creating a dedicated mapper object for
just 4 trivial `valueOf` calls. `CriteriaMapper` is now strictly sort-only, which matches its
remaining purpose.

**Alternatives considered:**

- Create a new `MangaAttributeUiMapper` — rejected as over-engineering for 4 one-liner functions.

---

### Decision: Fix `ReaderViewModel` nullable mismatch with `?: ""`

**What was decided:**
`chapter.volume ?: ""`, `chapter.number ?: ""`, `chapter.title ?: ""` added when copying into
`ReaderUiState`.

**Reasoning:**
`Chapter` domain model declares these fields as `String?` (optional metadata). `ReaderUiState` uses
`String` with `""` defaults. The `?: ""` fallback is consistent with the existing `""` defaults in
`ReaderUiState` and with how other nullable domain fields are handled throughout the codebase (e.g.,
`description ?: Manga.DEFAULT_DESCRIPTION` in `MangaUiMapper`).

**Alternatives considered:**

- Make `ReaderUiState` fields nullable — rejected because it would require null-checks across the UI
  layer and the empty-string default is semantically correct (renders nothing when unknown).

---

## 2026-03-18 (session 2)

### Decision: Split `domain/model/` into `domain/entity/` + `domain/value/`

**What was decided:**
All files in `domain/model/` were split into two new sub-packages:

- `domain/entity/` — ID-bearing business objects with identity (`Manga`, `Chapter`, `ChapterPages`,
  `FavoriteManga`, `Category`, `User`, `ReadingHistory`)
- `domain/value/` — Immutable, identity-free enums/value types (`MangaStatus`, `MangaContentRating`,
  `MangaLanguage`, `CategoryType`, `MangaSortCriteria`, `MangaSortOrder`, `ThemeMode`)

No class names changed; only package paths changed.

**Reasoning:**
Strict Clean Architecture (Uncle Bob) separates the domain's innermost ring into:

- **Entities**: objects with identity and lifecycle (carry an `id` field or are the central business
  object)
- **Value objects**: describe or qualify entities, are immutable, have no identity of their own

The previous `domain/model/` conflated both concepts in the same package, making it harder to reason
about the domain ring's structure. This split makes the architectural intent explicit.

**Classification rationale:**

- `Manga`, `Chapter`, `ChapterPages`, `FavoriteManga` → entity (have IDs, represent business
  objects)
- `Category` → entity (has `id: String`, referenced by `Manga`)
- `User`, `ReadingHistory` → entity (have identity, track user state)
- `MangaStatus`, `MangaContentRating`, `MangaLanguage` → value (enum descriptors of manga
  attributes)
- `CategoryType` → value (enum qualifier for Category)
- `MangaSortCriteria`, `MangaSortOrder` → value (query parameter descriptors, no identity)
- `ThemeMode` → value (UI/settings enum, no identity)

**Alternatives considered:**

- Keep `domain/model/` as-is — rejected, it conflates entity and value concepts
- Use `domain/entity/` only and put value types in `domain/model/` — rejected, inconsistent naming
- Nest values as inner classes of their entity — rejected, overly tight coupling and harder to reuse

**Implementation note:**
`Manga.kt` and `Category.kt` required manual cross-package imports added after the split, since
`MangaStatus`/`MangaContentRating`/`MangaLanguage` (used by `Manga`) and `CategoryType` (used by
`Category`) moved to a sibling package (`domain.value.*`) rather than staying in the same package.

---

## 2026-03-18 (session 3)

### Decision: `domain/repository/` and
`domain/usecase/` do NOT need structural changes after entity/value split

**What was decided:**
No structural (package/folder) changes are needed for `domain/repository/` or `domain/usecase/`
as a result of the `domain/model/` → `domain/entity/` + `domain/value/` split. Only import paths
were updated (already done by the bulk sed migration).

**Reasoning:**
The `entity`/`value` split is a **type taxonomy** — it answers "is this an identity-bearing object
or a pure descriptor?" Repositories and use cases are not types; they are operations and gateways.
Their sub-package organization by feature area (`manga/`, `category/`, `user/`, `settings/`) answers
a different and correct question: "what feature does this operation serve?"

In Clean Architecture:

- `domain/repository/` interfaces are gateways defined in the domain; their organization reflects
  *what data they manage*, not *what kind of type the result is*
- `domain/usecase/` classes are application-specific business rules; their organization reflects
  *what operation they perform*, not *what type their return value is*

Value objects appear in repositories only as *query parameters* (e.g.,
`statusFilter: List<MangaStatus>`)
— they describe the query, not the stored thing. This confirms the feature-area packaging is
correct.

**Verified by reading:**

- `MangaRepository.kt` — `import domain.entity.manga.Manga` ✓
- `FavoritesRepository.kt` — `import domain.entity.manga.FavoriteManga` ✓
- `GetMangaDetailsUseCase.kt` — `import domain.entity.manga.Manga` ✓
- `GetMangaListByCategoryUseCase.kt` — all 5 imports updated correctly ✓

**Alternatives considered:**

- Mirror the entity/value split in repository sub-packages (e.g., `repository/entity/manga/`) —
  rejected because repositories are operations/gateways, not type definitions; such nesting would
  be needless complexity

---

## 2026-03-19

### Decision: `presentation/value/` + `*Value` suffix for presentation value enums (supersedes
`enum/` plan)

**What was decided:**
The 7 presentation enum types should live in `presentation/value/` with `*Value` suffix:

- `MangaStatusValue`, `MangaLanguageValue`, `MangaContentRatingValue` → `presentation/value/manga/`
- `CategoryTypeValue` → `presentation/value/category/`
- `MangaSortCriteriaValue`, `MangaSortOrderValue` → `presentation/value/criteria/`
- `ThemeModeValue` → `presentation/value/settings/`

**Reasoning:**
Mirrors the domain layer's naming convention exactly:

- Domain: `domain/entity/` (entities) + `domain/value/` (value objects, e.g. `MangaStatus`,
  `ThemeMode`)
- Presentation: `presentation/model/` (data models) + `presentation/value/` (value enums)

This creates a clean parallel between the two layers: `domain/value/MangaStatus` ↔
`presentation/value/MangaStatusValue`.
The `*Value` suffix on the presentation side preserves the layer distinction (avoids name collision
with domain types in mapper files, e.g. `MangaStatus` vs `MangaStatusValue`).

**Supersedes:**
The written plan used `presentation/enum/` + `*Enum` suffix. This was implemented this session but
subsequently overridden by an intentional external edit to `MangaModel.kt` which uses `*Value`
naming.
The `enum/` + `*Enum` approach is discarded.

**Status:** Migration from `*Enum` → `*Value` still needs to be applied to ~30+ consumer files.
`MangaModel.kt` already uses `*Value` (the target state). `presentation/value/` directory not yet
created.

**Alternatives considered (from original plan):**

- `presentation/enum/` + `*Enum` — implemented then superseded; cleaner semantically for enums but
  breaks the domain/presentation symmetry
- `presentation/model/value/` — rejected (redundant nesting of `model/` in path)
- No suffix — rejected (name collision in mapper files between `MangaStatus` domain type and
  presentation type)

**Status (updated 2026-03-20):** Migration fully completed — all 7 `*Value` files exist in
`presentation/value/`. Previous blocker is resolved.

---

## 2026-03-20

### Decision: `MenuItem` data class → `MenuItemValue` enum in `presentation/value/menu/`

**What was decided:**
The `MenuItem` data class (3 mutable constructor params: `id`, `title`, `icon`) was replaced with a
`MenuItemValue` enum in `presentation/value/menu/`. The six fixed menu entries are now enum constants
with their display properties defined at declaration time.

**Final shape of `MenuItemValue` (after external edits this session):**
```kotlin
enum class MenuItemValue(@param:StringRes val nameRes: Int, val icon: ImageVector) {
  HOME, CATEGORIES, FAVORITES, HISTORY, PROFILE, SETTINGS;
  companion object {
    fun MenuItemValue.toNavRoute() = when(this) { HOME -> NavRoute.Home, ... }
  }
}
```
Note: original implementation had `titleRes` + computed `val id = name.lowercase()`. External edits
renamed `titleRes` → `nameRes`, removed `id`, and added `toNavRoute()` companion.

**Reasoning:**
- Six entries are fixed — a data class with runtime-constructed instances adds verbosity
- Matches the existing `ThemeModeValue` pattern for fixed-option UI enums
- `MenuDrawer` previously required 6 `stringResource()` calls + a 6-key `remember` — the enum
  collapses this to `remember { MenuItemValue.entries.toPersistentList() }`
- `toNavRoute()` companion makes navigation type-safe (no more `when (itemId)` string switches)

**Alternatives considered:**
- Keep `MenuItem` data class but extract constants — rejected, still requires runtime construction
- Place in `presentation/screens/common/menu/` package — rejected, value types belong in `value/`
  per established project convention

**Open question (still unresolved as of 2026-03-20 session 2):**
`MenuDrawer.kt` public signature uses `selectedItemId: String` but `MenuBody` now requires
`selectedItem: MenuItemValue`. Two paths forward:
- (a) Bridge internally with `entries.find { it.name.lowercase() == selectedItemId }`
- (b) Propagate `MenuItemValue` through `BaseScreen` and all callers for full type safety
No decision made — needs to be resolved at next session start.

**Status (updated 2026-03-20 session 3):** Resolved via type-safe navigation migration commit
`267105d`. `NavGraph.kt` and all callers updated; `MenuItemValue.toNavRoute()` companion used.

---

## 2026-03-20 (session 3)

### Decision: Mapper functions return nullable instead of throwing domain exceptions

**What was decided:**
`ChapterMapper.toChapter()` returns `Chapter?` (was `Chapter`, threw `ChapterNotFound`).
`ChapterPagesMapper.toChapterPages()` returns `ChapterPages?` (was `ChapterPages`, threw
`ChapterDataNotFound` twice). Domain exception throws moved to the calling repository.

**Reasoning:**
Mappers are pure data-transformation functions. Deciding that "null mangaId = ChapterNotFound" is a
**business rule** — that decision belongs in the repository (which understands the use-case context),
not in a mapper (which only knows how to transform shapes). This follows Clean Architecture's
Dependency Rule: mappers should not import domain exceptions.

For the chapter list use case specifically, one malformed API response item (missing `mangaId`)
should not abort the entire page. `mapNotNull` correctly skips it while preserving the rest.

**Alternatives considered:**
- Keep throws in mappers — rejected (architectural boundary violation: mapper imports domain exception)
- Throw in mapper, catch in repo — rejected (still couples mapper to domain exception hierarchy)

---

### Decision: `FavoriteMangaModel` (5 fields) instead of `MangaModel` (13 fields) for Favorites

**What was decided:**
Created `presentation/model/manga/FavoriteMangaModel.kt` with only the 5 fields `FavoriteManga`
actually carries: `id`, `title`, `coverUrl`, `author`, `status`. `FavoritesContent` uses a new
`FavoriteMangaItem` composable instead of the shared `MangaItem`/`VerticalGridMangaList`.

**Reasoning:**
The old `FavoriteManga.toMangaModel()` produced a `MangaModel` with 7 hollow `""` / `persistentListOf()`
values (`description`, `artist`, `categories`, `contentRating`, `year`, `availableLanguages`,
`latestChapter`, `updatedAt`). These hollows are never rendered in `FavoritesContent` — `MangaItem`
only shows `id`, `title`, `coverUrl`, `author`, `status`. Using a 13-field model for a 5-field use
case is a type-partitioning mistake: it implies the missing fields exist but are empty, rather than
clearly communicating "this model only has these 5 fields."

**Alternatives considered:**
- Keep `MangaModel` with hollow fields — rejected (misleading contract, wastes allocation)
- Add `FavoriteManga.author/status` to a lighter wrapper of `MangaModel` — rejected (unnecessary
  indirection; a dedicated model is clearer)
- Reuse `MangaItem` via a conversion — rejected (would require fake hollow fields to construct `MangaModel`)

---

### Decision: `UserError.Unexpected` replaces `null` return from `toUserError()`

**What was decided:**
Added `data object Unexpected : UserError(R.string.oops_something_went_wrong_please_try_again)` to
`UserError`. `ErrorMapper.toUserError()` now returns `UserError` (non-nullable); `else -> null`
replaced with `else -> UserError.Unexpected`.

**Reasoning:**
A nullable return type on `toUserError()` leaks a null contract that all 3 auth VMs must silently
accommodate via `else ->` (i.e., the null case is handled implicitly by doing nothing). Making the
return non-nullable with an explicit `Unexpected` sentinel:
1. Makes the unrecognized-exception case explicit and visible in the type system
2. Allows auth VMs to show a generic error instead of silently ignoring unexpected exceptions
3. Is consistent with how `toFeatureError()` already works (always returns `FeatureError`, never null)

**Alternatives considered:**
- Keep nullable — rejected (implicit null handling is a hidden contract)
- Throw in `else` branch — rejected (would change caller behavior significantly; `Unexpected` is safer)

---

### Decision: `String?.toMangaLanguage()` uses nullable receiver (consistency fix)

**What was decided:**
`ApiParamMapper.toMangaLanguage()` receiver changed from `String` to `String?`. Returns
`MangaLanguage.UNKNOWN` for null input (handled by `?: MangaLanguage.UNKNOWN` fallback when no param
entry matches null).

**Reasoning:**
`toMangaStatus()` and `toMangaContentRating()` already use `String?` receivers and handle null
gracefully. `toMangaLanguage()` using a non-nullable receiver was inconsistent — callers had to add
`?.` or `!!` before calling on nullable strings. `attributes?.translatedLanguage.toMangaLanguage()`
is cleaner than `attributes?.translatedLanguage?.toMangaLanguage() ?: Chapter.DEFAULT_LANGUAGE`.

`MangaLanguage.UNKNOWN == Chapter.DEFAULT_LANGUAGE` (by value), so the behavioral outcome is
identical — this is purely a type-consistency improvement.

**Alternatives considered:**
- Keep non-nullable, require callers to use `?.` — rejected (inconsistent with sibling functions)

---

## 2026-03-20 (session 4 — domain layer audit)

### Decision: `BusinessException`/`InfrastructureException` subtypes → `data class` with `rootCause`

**What was decided:**
All concrete exception subtype classes (`class`) converted to `data class` with a `val rootCause: Throwable? = null` constructor parameter that is forwarded as the positional `cause` argument to the parent.

**Reasoning:**
`data class` provides structural equality (two `InvalidCredentials(rootCause = null)` are equal), which makes exceptions predictable in `when` branches and tests. The renamed parameter `rootCause` avoids shadowing `Throwable.cause` (a JVM property) and makes the extra field explicit in `toString()` output.

**Alternatives considered:**
- Keep `class` — rejected (no structural equality, no `toString` with cause)
- `data class` with `cause` name — rejected (`cause` shadows the inherited `Throwable.cause` property)

---

### Decision: `ValidationException` subtypes → `data object`

**What was decided:**
All 9 leaf subtypes (`Email.Empty`, `Email.Invalid`, `Password.Empty`, `Password.TooWeak`, `ConfirmPassword.Empty`, `ConfirmPassword.Mismatch`, `Name.Empty`) converted from `class` to `data object`. Base class `cause: Throwable? = null` parameter removed; `cause = null` hardcoded.

**Reasoning:**
Validation exceptions are thrown from domain model validation (`User.validate*()`), never from catching external exceptions. They carry no extra data — just the message. `data object` is the correct Kotlin type for a stateless singleton signal. Removing the `cause` parameter from the base class makes the contract explicit: validation exceptions have no cause.

**Alternatives considered:**
- Keep `class` — rejected (unnecessary instantiation of identical objects; misleading `cause` parameter)
- Keep base `cause` param but unused — rejected (dead parameter invites misuse)

---

### Decision: `MangaLanguage.THAILAND` → `TAGALOG`

**What was decided:**
Renamed `THAILAND` to `TAGALOG` across all three layers: `MangaLanguage` (domain), `MangaLanguageCodeParam` (data), `MangaLanguageValue` (presentation), and `strings_lang_generated.xml`.

**Reasoning:**
ISO 639-1 code `"tl"` is Tagalog (a Philippine language), not Thai. Thai is ISO `"th"`, already correctly represented as `THAI`. The entry was semantically wrong and would show "Thailand" (a country name, not a language) in the language selection UI.

**Alternatives considered:**
- None — this was a factual error, not a design tradeoff.

---

### Decision: `ClearExpiredCacheUseCase.clock` as `internal var`

**What was decided:**
`System.currentTimeMillis()` replaced with `internal var clock: () -> Long = System::currentTimeMillis`. Tests can override `clock` without touching the Hilt DI graph.

**Reasoning:**
Hilt cannot inject `() -> Long` (a function type) without a dedicated `@Provides` binding. A constructor parameter would require adding a Hilt module for a single test-only concern. `internal var` achieves testability within the module with minimal boilerplate. Using `System::currentTimeMillis` (method reference) is idiomatic Kotlin vs `{ System.currentTimeMillis() }`.

**Trade-off acknowledged:**
`internal var` is mutable from anywhere in the module — tests must reset the value after each test case to avoid cross-test contamination. This is a testing discipline constraint, not a runtime risk.

**Alternatives considered:**
- Constructor injection + Hilt `@Provides` — rejected (adds a module for one test-only use)
- `@VisibleForTesting` annotation — considered acceptable addition but not strictly needed given `internal`

---

### Decision: `HistoryRepository.addAndUpdateToHistory` → `upsertHistory`

**What was decided:**
Repository interface method and impl override renamed from `addAndUpdateToHistory` to `upsertHistory`.

**Reasoning:**
The Firestore source already called `upsertHistory` internally. The mismatch between domain interface name and implementation was a naming inconsistency. "Upsert" is the technically precise term for insert-or-update semantics. `AddAndUpdateToHistoryUseCase` class was also renamed to `UpsertHistoryUseCase` for consistency.

**Alternatives considered:**
- Keep `addAndUpdateToHistory` — rejected (verbose, technically imprecise, inconsistent with impl)

---

### Decision: `ChapterPages.mangaId` — add field, remove from `CacheRepository` interface

**What was decided:**
`val mangaId: String` added to `ChapterPages` domain entity. `CacheRepository.addChapterCache(mangaId, chapterPages)` changed to `addChapterCache(chapterPages)` — the impl reads `chapterPages.mangaId`. `ChapterRepository.getChapterPages(chapterId)` changed to `getChapterPages(chapterId, mangaId)` so callers supply `mangaId` when constructing `ChapterPages` from the network response.

**Reasoning:**
`mangaId` was a storage indexing concern (`ChapterCacheEntity` denormalizes it for group-delete queries). A chapter's pages unambiguously belong to one manga — `mangaId` is a legitimate domain fact, not a storage detail. Carrying it on the domain type means `CacheRepository` no longer needs it as a separate parameter, fully removing the leakage.

**Chain of changes:**
- `ChapterPagesMapper.toChapterPages(chapterId, mangaId)` — added `mangaId` param
- `ChapterPagesMapper.toChapterCacheEntity()` — removed `mangaId` param; uses `chapterPages.mangaId`
- `ChapterCacheEntity.toChapterPages()` — populates `mangaId` from entity field
- `GetChapterPagesUseCase.invoke(chapterId, mangaId)` — added `mangaId`
- `AddChapterCacheUseCase.invoke(chapterPages)` — removed `mangaId`
- `ReaderViewModel`: `getChapterPagesUseCase(chapterId, mangaId)`, `addChapterCacheUseCase(chapterPages)`

**Alternatives considered:**
- Keep `mangaId` in `CacheRepository` interface — rejected (storage concern leaking into domain)
- Look up `mangaId` inside repo via `getChapterDetails` — rejected (extra network call, unacceptable overhead)
- Add `mangaId` to `ChapterPages` but keep interface unchanged — rejected (defeats the purpose)

---

### Decision: `ReadingHistory.findContinueTarget` — fix off-by-one (`pageCount - 1` → `pageCount`)

**What was decided:**
Condition changed from `it.lastReadPage < it.pageCount - 1` to `it.lastReadPage < it.pageCount`.

**Reasoning:**
Pages in this app are 1-indexed (`FIRST_PAGE = 1`). For a chapter with `pageCount = N`, the last page index is `N`. The old condition `lastReadPage < pageCount - 1` would exclude a reader stopped at page `N - 1` (penultimate page) — treating an in-progress chapter as complete and not offering it as a continue target.

**Alternatives considered:**
- None — this was a factual bug given the established 1-indexed page convention.

---

### Decision: `ValidationException` base class — remove dead `cause` parameter

**What was decided:**
`ValidationException(message: String?, cause: Throwable? = null)` changed to `ValidationException(message: String)` with `cause = null` hardcoded in the `DomainException(message, cause = null)` delegation.

**Reasoning:**
`ValidationException` subtypes are thrown from domain validation logic, never from catching external exceptions. Exposing a `cause` parameter implies a caller could (and might) pass a `Throwable`, contradicting the invariant. The base class parameter was dead code — no subtype used it, and no call site passed a cause.

**Alternatives considered:**
- Keep dead parameter — rejected (misleading contract; invites misuse)

---

## 2026-03-20 (session 5 — domain layer pass 3)

### Decision: Do NOT add companion constants to domain entities unless there is an actual call site

**What was decided:**
`Chapter.DEFAULT_MANGA_ID`, `ChapterPages.DEFAULT_BASE_URL`, `ChapterPages.DEFAULT_HASH`,
`Manga.DEFAULT_STATUS`, and `Manga.DEFAULT_LAST_UPDATED` were staged by Pass 3 review agents and
then reverted after the user verified they had zero usage in the codebase.

**Reasoning:**
The rule "entities own their fallback values as companion object constants" only applies when
a fallback is actually needed. In this codebase, mappers for `Chapter`, `ChapterPages`, and `Manga`
use the **early-exit pattern** (`?: return null`) for mandatory fields rather than defaulting to
empty values. There is no scenario where a `Chapter` without a `mangaId` is constructed with a
default — the mapper simply discards that response item. Similarly, `MangaStatus.UNKNOWN` is handled
internally by `ApiParamMapper.toMangaStatus()` and is never referenced via a companion constant.
Adding unused constants is dead code — it implies a fallback path that doesn't exist and misleads
future readers.

**The existing constants that ARE used and correct:**
`Manga.DEFAULT_TITLE`, `Manga.DEFAULT_DESCRIPTION`, `Manga.DEFAULT_AUTHOR`, `Manga.DEFAULT_ARTIST`,
`Manga.DEFAULT_YEAR`, `Manga.DEFAULT_LAST_CHAPTER` (all used in `MangaMapper`/`presentation/mapper/MangaMapper`),
`Chapter.DEFAULT_TITLE`, `Chapter.DEFAULT_CHAPTER_NUMBER`, `Chapter.DEFAULT_VOLUME` (used in `presentation/mapper/ChapterMapper`),
`FavoriteManga.DEFAULT_ADDED_AT`, `ReadingHistory.DEFAULT_LAST_READ_AT` (used in use cases),
`User.DEFAULT_NAME`, `User.DEFAULT_EMAIL` (used in `UserMapper`),
`Category.DEFAULT_TITLE`, `Category.DEFAULT_TYPE` (used in `CategoryMapper`).

**How to apply:**
Before adding a companion constant to a domain entity, grep for the constant name across the
codebase to confirm there is at least one actual call site. If grep returns nothing, do not add it.

**Alternatives considered:**
- Keep the constants "for future use" — rejected. Dead code is a maintenance burden and a false
  signal about the codebase's fallback behavior.

---

### Decision: `CacheRepository.clearExpiredCache(expiryTimestamp)` → `clearExpiredCache(olderThan)`

**What was decided:**
Parameter renamed from `expiryTimestamp` to `olderThan` in the domain interface and data layer implementation.

**Reasoning:**
`expiryTimestamp` leaks the arithmetic detail ("this is a computed timestamp representing expiry time") into the domain contract. The domain concept is "clear cache entries older than this point in time." `olderThan` communicates the intent at the call site without requiring the reader to mentally model the timestamp computation. Small naming improvement with no behavioral change.

**Alternatives considered:**
- Keep `expiryTimestamp` — rejected (describes the computed value, not the intent)

---

### Decision: `MangaRepository.searchManga` — add `offset: Int = 0` default

**What was decided:**
`searchManga(query: String, offset: Int, limit: Int = 20)` → `searchManga(query: String, offset: Int = 0, limit: Int = 20)`.

**Reasoning:**
`limit` already had a default value of 20. `offset` having no default was an inconsistency: callers
that want the first page had to explicitly pass `0` while callers that want the default page size
could omit `limit`. Adding `offset: Int = 0` makes the interface consistent — both pagination
parameters are optional when the caller wants the first page with the default page size.

**Alternatives considered:**
- Keep offset required — rejected (inconsistent with limit's default; callers always pass 0 for the first page)
