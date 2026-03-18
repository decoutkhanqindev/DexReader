# Current Task

## Status: COMPLETED (pending build verification)

The primary task — splitting `domain/model` into `domain/entity` + `domain/value` — is fully
implemented. This session also included an architectural Q&A (no code changes).

## What Was Done
All 14 domain model files were moved and had their `package` declarations updated:

### Entities (→ domain/entity/)
- `domain/entity/manga/Manga.kt`
- `domain/entity/manga/Chapter.kt`
- `domain/entity/manga/ChapterPages.kt`
- `domain/entity/manga/FavoriteManga.kt`
- `domain/entity/category/Category.kt`
- `domain/entity/user/User.kt`
- `domain/entity/user/ReadingHistory.kt`

### Value Objects (→ domain/value/)
- `domain/value/manga/MangaStatus.kt`
- `domain/value/manga/MangaContentRating.kt`
- `domain/value/manga/MangaLanguage.kt`
- `domain/value/category/CategoryType.kt`
- `domain/value/criteria/MangaSortCriteria.kt`
- `domain/value/criteria/MangaSortOrder.kt`
- `domain/value/settings/ThemeMode.kt`

## Important Context
- Two extra intra-domain cross-package imports were added manually:
  - `Manga.kt` needed explicit imports for `MangaStatus`, `MangaContentRating`, `MangaLanguage`
    (previously same-package, now in `domain.value.manga`)
  - `Category.kt` needed explicit import for `CategoryType` (now in `domain.value.category`)
- ~64 consuming files across data, domain, and presentation layers had their import statements updated
  via bulk sed replacement
- `domain/model/` directory no longer exists
- **Build has NOT been run yet** — `./gradlew assembleDebug` is the pending verification step

## Architectural Q&A This Session
Confirmed via code inspection that `domain/repository/` and `domain/usecase/` do NOT need structural
changes after the entity/value split. Their import paths were already updated by the bulk sed.
Their sub-package organization (by feature area: manga/, category/, user/, settings/) is correct
and answers a different question than the entity/value taxonomy.
