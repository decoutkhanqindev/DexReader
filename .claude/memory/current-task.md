# Current Task

## Task
Presentation Model Cleanup — Status & ContentRating UiModels

The previous session promoted `MangaStatus` and `MangaContentRating` from `domain/model/criteria/filter/` (named `MangaStatusFilter`, `MangaContentRatingFilter`) to `domain/model/manga/` as proper manga attributes. The domain layer is clean. This session updated the presentation layer to match.

## Status
**COMPLETED** — All files updated, old files deleted, build error fixed.

## Last Fix Applied
`ReaderViewModel.kt` lines 127-129: Added `?: ""` fallbacks for nullable `Chapter.volume`, `Chapter.number`, `Chapter.title` when copying into `ReaderUiState` (which expects non-null `String`).

File: `app/src/main/java/com/decoutkhanqindev/dexreader/presentation/screens/reader/ReaderViewModel.kt`
