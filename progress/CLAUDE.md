# DexReader — Claude Code Session Notes

## Status

**Current Phase:** Presentation Layer — Full Domain Model Isolation
**Overall Progress:** ALL 8 BATCHES COMPLETE ✅ — Build verified (`BUILD SUCCESSFUL`, 0 errors)

---

## Completed This Session

### Batch 3 (User) — remaining gap closed

- `UserViewModel`: private `_domainUserProfile: MutableStateFlow<User?>` → exposes
  `userProfile: StateFlow<UserUiModel?>` via `.map { it?.toUserUiModel() }.stateIn(Eagerly)`
- `UserUiMapper`: added reverse mapper `UserUiModel.toDomainUser(): User` (needed by
  `ProfileViewModel` for `UpdateUserProfileUseCase`)
- `ProfileViewModel`: `updateCurrentUser(User?)` → `updateCurrentUser(UserUiModel?)`, stores domain
  `User` internally via `toDomainUser()`
- `NavGraph.kt`, `BaseScreen.kt`, `MenuDrawer.kt`, `MenuHeader.kt`: `User?` → `UserUiModel?`
- All 8 `*Screen.kt` files: `currentUser: User?` → `currentUser: UserUiModel?`

### Batches 4+5+6 (Manga/Chapter/ReadingHistory) — done previous session

- All domain types removed from composables/UiStates
- `FavoritesContent` CA violation fixed (data-layer mapper import removed)
- `FavoriteMangaMapper.toManga()` deleted

### Build result

`./gradlew :app:compileDebugKotlin` → `BUILD SUCCESSFUL in 10s`, 0 errors, 0 new warnings

---

## Next Session — Start Here

**The plan is complete. No pending refactoring from this plan.**

Suggested next work:

1. Run `./gradlew :app:assembleDebug` for full APK build verification
2. Manual smoke test each screen (Home, Categories, Manga Details, Reader, History, Favorites,
   Profile)
3. Commit staged changes if not yet committed

---

## Important Context

### Domain isolation — final rule

- Only `domain.*` imports allowed in: **ViewModels** (translation boundary) + **presentation/mapper/
  ** objects
- No composable, Screen, or UiState file may import `domain.*`
- `UserUiMapper` uniquely has both `User.toUserUiModel()` AND `UserUiModel.toDomainUser()` — the
  reverse mapper is needed because `UpdateUserProfileUseCase` takes a domain `User`

### Private domain retention pattern (3 cases)

1. `MangaDetailsViewModel._domainManga: MutableStateFlow<Manga?>` — for `addToFavoritesUseCase`
2. `MangaDetailsViewModel._readingHistoryList: MutableStateFlow<List<ReadingHistory>>` — for
   `ReadingHistory.findContinueTarget()` domain logic; public
   `readingHistoryList: StateFlow<List<ReadingHistoryUiModel>>` exposed via `.map { }.stateIn(...)`
3. `ProfileViewModel.currentDomainUser: User?` — for `UpdateUserProfileUseCase`; set via
   `value?.toDomainUser()`
4. `UserViewModel._domainUserProfile: MutableStateFlow<User?>` — for internal use; public
   `userProfile: StateFlow<UserUiModel?>` via `.map { }.stateIn(Eagerly)`

### Note on MEMORY.md exception naming

Memory.md says `RemoteException`/`MangaException`. The actual code uses different names. Do not
change exceptions based on memory.md.

---

## Files Modified This Session (Batch 3 completion)

- `presentation/mapper/UserUiMapper.kt` — added `UserUiModel.toDomainUser()`
- `presentation/UserViewModel.kt` — private `_domainUserProfile`, public `StateFlow<UserUiModel?>`
- `presentation/screens/profile/ProfileViewModel.kt` — `updateCurrentUser(UserUiModel?)`
- `presentation/navigation/NavGraph.kt` — `UserUiModel?`
- `presentation/screens/common/base/BaseScreen.kt` — `UserUiModel?`
- `presentation/screens/common/menu/MenuDrawer.kt` — `UserUiModel?`
- `presentation/screens/common/menu/MenuHeader.kt` — `UserUiModel?`
- `presentation/screens/home/HomeScreen.kt` — `UserUiModel?`
- `presentation/screens/categories/CategoriesScreen.kt` — `UserUiModel?`
- `presentation/screens/settings/SettingsScreen.kt` — `UserUiModel?`
- `presentation/screens/favorites/FavoritesScreen.kt` — `UserUiModel?`
- `presentation/screens/history/HistoryScreen.kt` — `UserUiModel?`
- `presentation/screens/profile/ProfileScreen.kt` — `UserUiModel?`
- `presentation/screens/manga_details/MangaDetailsScreen.kt` — `UserUiModel?`
- `presentation/screens/reader/ReaderScreen.kt` — `UserUiModel?`
