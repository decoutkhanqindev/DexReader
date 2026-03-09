## Status

Refactoring of the `presentation` layer to use `ImmutableList<T>` instead of standard Kotlin `List<T>` is complete. Project successfully compiles.

## Completed This Session

- Added `kotlinx-collections-immutable` dependency explicitly.
- Automated bulk replacement of standard `List`, `emptyList`, `listOf`, and `toList` constructions to `ImmutableList` equivalents within the Jetpack Compose `presentation` layer state flows.
- Automated adding `@Immutable` tags to complex UI state definitions.
- Resolved Kotlin generic / explicit typing compilation exceptions tied to `Flow` aggregations (`CategoriesViewModel`), `.addAll()` mutable collections conversions (`MangaDetailsViewModel`, `ReaderViewModel`), and standard enumerations mappings.

## Next Session - Start Here

Review and verify application behavior manually. Test UI views that use heavily-nested states (Reader view, Manga list mappings, and Grid categories) to ensure recomposition metrics show an improvement or maintain parity. `ReaderViewModel.kt` or `MangaDetailsViewModel.kt` are central points to start reading from.

## Important Context

- **Jetpack Compose Stability:** We deliberately ported all lists inside data models referenced by Compose functions to `ImmutableList` to avoid redundant recomposition passes caused by Java `List` stability inference issues.
- **Handling Mutable Operations:** When encountering code attempting to invoke `.addAll()` on immutable state variables, we standardized to combining values then wrapping them sequentially `(oldList + newList).toPersistentList()`.
- **Enum entries mapping:** `EnumEntries` must be mapped into `.toPersistentList()` whenever injected into `ImmutableList` properties manually.

## Files Modified

- `app/src/main/java/com/decoutkhanqindev/dexreader/presentation/screens/reader/ReaderViewModel.kt`
- `app/src/main/java/com/decoutkhanqindev/dexreader/presentation/screens/categories/CategoriesViewModel.kt`
- `app/src/main/java/com/decoutkhanqindev/dexreader/presentation/screens/manga_details/MangaDetailsViewModel.kt`
- `app/src/main/java/com/decoutkhanqindev/dexreader/presentation/screens/favorites/FavoritesViewModel.kt`
- `app/src/main/java/com/decoutkhanqindev/dexreader/presentation/screens/home/HomeViewModel.kt`
- `app/src/main/java/com/decoutkhanqindev/dexreader/presentation/mapper/MangaUiMapper.kt`
- ...and roughly 45 other UI State data classes / Composables in the `presentation` directory.
