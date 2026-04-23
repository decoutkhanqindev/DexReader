# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## IMPORTANT: Keep This File in Sync

**Whenever any file in the `di/` layer is added, removed, or modified, this CLAUDE.md must be updated in the same session to reflect the change.** This includes:

- New Hilt module added → update module list and package tree
- New `@Binds` or `@Provides` binding added → update the relevant module section
- New Firebase source bound → update FirebaseModule section
- Module type changed (`object` ↔ `interface`) → update Module Patterns section
- DI scope changed (e.g., new non-singleton binding) → update overview note

Do not leave this file stale. An outdated CLAUDE.md is worse than no CLAUDE.md.

## DI Layer Overview

4 Hilt modules, all `@InstallIn(SingletonComponent::class)` — every binding is application-scoped (`@Singleton`). There are no scoped (activity/fragment/viewmodel) components.

```
di/
├── LocalModule.kt       object — @Provides Room + DataStore
├── RepositoryModule.kt  interface — @Binds 8 repository interface → impl
└── network/
    ├── ApiModule.kt     object — @Provides Moshi, OkHttp, Retrofit, ApiService
    └── FirebaseModule.kt  object — @Provides FirebaseAuth, FirebaseFirestore + 4 source bindings
```

---

## Module Patterns

**`RepositoryModule`** is an `interface` (not `object`) — this is intentional. Hilt `@Binds` functions must be abstract, so the module must be an abstract class or interface. All 8 repository domain interfaces are bound here to their data-layer implementations.

**`LocalModule`, `ApiModule`, `FirebaseModule`** are `object` — used for `@Provides` functions that require construction logic.

**Firebase sources** use `@Provides` instead of `@Binds` even though they follow the interface→impl pattern. This is because the impls receive `FirebaseAuth`/`FirebaseFirestore` via their own `@Inject constructor`, and Hilt resolves the chain automatically — no manual wiring needed inside the provide function.

---

## Dependency Graph

```
ApiModule
  Moshi (IsoDateTimeAdapter + KotlinJsonAdapterFactory)
    └─ MoshiConverterFactory
  HttpLoggingInterceptor (BODY in DEBUG, NONE in RELEASE)
  NetworkInterceptor (@Inject constructor — auto-resolved)
  OkHttpClient (NetworkInterceptor + HttpLoggingInterceptor, 30s timeouts)
  Retrofit (OkHttpClient + Moshi, BASE_URL from BuildConfig)
    └─ ApiService

FirebaseModule
  FirebaseAuth.getInstance()
  FirebaseFirestore.getInstance()
  FirebaseAuthSource ← FirebaseAuthSourceImpl(@Inject: FirebaseAuth)
  FirebaseUserFirestoreSource ← FirebaseUserFirestoreSourceImpl(@Inject: FirebaseFirestore)
  FirebaseFavoriteFirestoreSource ← FirebaseFavoriteFirestoreSourceImpl(@Inject: FirebaseFirestore)
  FirebaseHistoryFirestoreSource ← FirebaseHistoryFirestoreSourceImpl(@Inject: FirebaseFirestore)

LocalModule
  ChapterCacheDatabase(@ApplicationContext, CHAPTER_CACHE_DB_NAME, fallbackToDestructiveMigration)
  ChapterCacheDao ← ChapterCacheDatabase.chapterCacheDao()
  DataStore<Preferences> ← context.dataStore (ThemePrefsManager extension property)

RepositoryModule (@Binds)
  MangaRepository ← MangaRepositoryImpl(@Inject: ApiService)
  ChapterRepository ← ChapterRepositoryImpl(@Inject: ApiService)
  CacheRepository ← CacheRepositoryImpl(@Inject: ChapterCacheDao)
  CategoryRepository ← CategoryRepositoryImpl(@Inject: ApiService)
  UserRepository ← UserRepositoryImpl(@Inject: FirebaseAuthSource, FirebaseUserFirestoreSource)
  FavoritesRepository ← FavoritesRepositoryImpl(@Inject: FirebaseFavoriteFirestoreSource)
  HistoryRepository ← HistoryRepositoryImpl(@Inject: FirebaseHistoryFirestoreSource)
  SettingsRepository ← SettingsRepositoryImpl(@Inject: DataStore<Preferences>)
```

---

## Adding a New Binding

**New repository:** Add one `@Binds @Singleton` abstract fun to `RepositoryModule`.

**New Firebase source:** Add a `@Provides @Singleton` fun to `FirebaseModule` that takes the impl as a parameter and returns the interface type. The impl's `@Inject constructor` receives `FirebaseAuth` or `FirebaseFirestore` — both are already provided.

**New local data source:** Add `@Provides @Singleton` funs to `LocalModule`. If it's a new Room database, also add a DAO provider that delegates to `db.newDao()`.

**No new module files needed** unless a new distinct infrastructure concern (e.g., a second REST API) is introduced — in that case create a new `object` module in `di/network/`.

---

## Key Configuration Details

- `BASE_URL` and `UPLOAD_URL` come from `BuildConfig`, which reads from `local.properties` at build time.
- Moshi adapter registration order matters: `IsoDateTimeAdapter` must be added **before** `KotlinJsonAdapterFactory` so the custom `Long?` adapter takes priority over the reflection fallback.
- Room uses `fallbackToDestructiveMigration(true)` — chapter cache data is ephemeral and re-fetchable, so destructive migration on schema change is acceptable.
- `NetworkInterceptor` is currently a pass-through — it is injected via `@Inject constructor` and automatically resolved by Hilt without an explicit `@Provides`.
