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

4 Hilt modules, all `@InstallIn(SingletonComponent::class)` — every binding is application-scoped (`@Singleton`). There are no scoped (activity/fragment/viewmodel) components. Total: **22 bindings**.

```
di/
├── LocalModule.kt        object    — 3 @Provides: Room + DataStore
├── RepositoryModule.kt   interface — 8 @Binds:  all repository interface → impl
└── network/
    ├── ApiModule.kt      object    — 5 @Provides: Moshi, OkHttp, Retrofit, ApiService
    └── FirebaseModule.kt object    — 6 @Provides: FirebaseAuth, FirebaseFirestore + 4 sources
```

---

## Module Patterns

**`RepositoryModule`** is an `interface` (not `object`) — Hilt `@Binds` functions must be abstract, which requires an abstract class or interface. All 8 repository domain interfaces are bound here to their data-layer implementations.

**`LocalModule`, `ApiModule`, `FirebaseModule`** are `object` — used for `@Provides` functions that require construction logic (calling builders, `getInstance()`, etc.).

**Firebase sources** use `@Provides` instead of `@Binds` even though they follow the interface→impl pattern. This is because the impls receive `FirebaseAuth`/`FirebaseFirestore` via their own `@Inject constructor`, and Hilt resolves the chain automatically — Hilt injects the impl's constructor dependencies, then the `@Provides` function receives the fully-constructed impl as a parameter. No manual wiring needed inside the provide function.

---

## LocalModule

```kotlin
@Module @InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides @Singleton
    fun provideChapterCacheDB(@ApplicationContext context: Context): ChapterCacheDatabase

    @Provides @Singleton
    fun provideChapterCacheDao(db: ChapterCacheDatabase): ChapterCacheDao

    @Provides @Singleton
    fun provideThemePrefsManager(@ApplicationContext context: Context): DataStore<Preferences>
}
```

**Notable details:**
- `ChapterCacheDatabase` is built with `fallbackToDestructiveMigration(true)` — chapter cache is ephemeral and re-fetchable from network, so destructive schema migration is acceptable.
- Database name is `ChapterCacheDatabase.CHAPTER_CACHE_DB_NAME` (constant defined in the entity class).
- `DataStore<Preferences>` is provided via `context.dataStore` — a Kotlin extension property defined in `ThemePrefsManager.kt`. It is a delegate-backed DataStore keyed to a fixed file name.
- `ChapterCacheDao` is derived from the database instance via `db.chapterCacheDao()`.

---

## RepositoryModule

```kotlin
@Module @InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds @Singleton fun bindMangaRepository(impl: MangaRepositoryImpl): MangaRepository
    @Binds @Singleton fun bindChapterRepository(impl: ChapterRepositoryImpl): ChapterRepository
    @Binds @Singleton fun bindCacheRepository(impl: CacheRepositoryImpl): CacheRepository
    @Binds @Singleton fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository
    @Binds @Singleton fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
    @Binds @Singleton fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository
    @Binds @Singleton fun bindHistoryRepository(impl: HistoryRepositoryImpl): HistoryRepository
    @Binds @Singleton fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
```

**Binding table:**

| Domain Interface | Data Impl | Impl's `@Inject` dependencies |
|---|---|---|
| `MangaRepository` | `MangaRepositoryImpl` | `ApiService` |
| `ChapterRepository` | `ChapterRepositoryImpl` | `ApiService` |
| `CacheRepository` | `CacheRepositoryImpl` | `ChapterCacheDao` |
| `CategoryRepository` | `CategoryRepositoryImpl` | `ApiService` |
| `UserRepository` | `UserRepositoryImpl` | `FirebaseAuthSource`, `FirebaseUserFirestoreSource` |
| `FavoritesRepository` | `FavoritesRepositoryImpl` | `FirebaseFavoriteFirestoreSource` |
| `HistoryRepository` | `HistoryRepositoryImpl` | `FirebaseHistoryFirestoreSource` |
| `SettingsRepository` | `SettingsRepositoryImpl` | `DataStore<Preferences>` |

---

## ApiModule

```kotlin
@Module @InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides @Singleton
    fun provideMoshi(): Moshi

    @Provides @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor

    @Provides @Singleton
    fun provideOkHttpClient(
        authorizationInterceptor: NetworkInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient

    @Provides @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit

    @Provides @Singleton
    fun provideMangaDexApiService(retrofit: Retrofit): ApiService
}
```

**Notable details:**
- **Moshi adapter order matters:** `IsoDateTimeAdapter` is added **before** `KotlinJsonAdapterFactory`. Custom adapters must be registered first so they take priority over the reflection fallback. `IsoDateTimeAdapter` parses ISO 8601 date strings → `Long?` (epoch millis).
- **OkHttp timeouts:** 30 seconds for `connectTimeout`, `readTimeout`, and `writeTimeout`.
- **Logging level:** `HttpLoggingInterceptor.Level.BODY` in DEBUG builds; `Level.NONE` in release builds (controlled by `BuildConfig.DEBUG`).
- **`NetworkInterceptor`** is resolved automatically by Hilt via its `@Inject constructor` — no explicit `@Provides` needed. It is added as the first interceptor in OkHttp (before logging).
- **Base URL:** `BuildConfig.BASE_URL`, sourced from `local.properties` at build time.
- **Converter:** `MoshiConverterFactory.create(moshi)` — uses the `Moshi` instance from above.

**Dependency chain (construction order):**
```
IsoDateTimeAdapter + KotlinJsonAdapterFactory → Moshi
BuildConfig.DEBUG → HttpLoggingInterceptor (level)
NetworkInterceptor (@Inject auto-resolved) ┐
HttpLoggingInterceptor                     ├→ OkHttpClient (30s timeouts)
OkHttpClient + Moshi → Retrofit (BASE_URL)
Retrofit → ApiService
```

---

## FirebaseModule

```kotlin
@Module @InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides @Singleton fun provideFirebaseAuth(): FirebaseAuth
    @Provides @Singleton fun provideFirebaseFirestore(): FirebaseFirestore

    @Provides @Singleton
    fun provideFirebaseAuthSource(impl: FirebaseAuthSourceImpl): FirebaseAuthSource

    @Provides @Singleton
    fun provideFirebaseUserFirestoreSource(impl: FirebaseUserFirestoreSourceImpl): FirebaseUserFirestoreSource

    @Provides @Singleton
    fun provideFirebaseFavoriteFirestoreSource(impl: FirebaseFavoriteFirestoreSourceImpl): FirebaseFavoriteFirestoreSource

    @Provides @Singleton
    fun provideFirebaseHistoryFirestoreSource(impl: FirebaseHistoryFirestoreSourceImpl): FirebaseHistoryFirestoreSource
}
```

**Notable details:**
- `FirebaseAuth` and `FirebaseFirestore` are obtained via `getInstance()` — no constructor arguments.
- All 4 Firebase source impls receive `FirebaseAuth` or `FirebaseFirestore` through their own `@Inject constructor`. Hilt injects those dependencies automatically before passing the impl to the `@Provides` function.
- Why `@Provides` instead of `@Binds` for sources: `@Binds` requires the impl to be directly injectable without any extra wiring — but since the impls depend on `FirebaseAuth`/`FirebaseFirestore` which are also provided here, using `@Provides` makes the resolution order explicit and avoids potential Hilt circular-dependency analysis issues.

**Source binding table:**

| Interface | Impl | Impl's `@Inject` dependency |
|---|---|---|
| `FirebaseAuthSource` | `FirebaseAuthSourceImpl` | `FirebaseAuth` |
| `FirebaseUserFirestoreSource` | `FirebaseUserFirestoreSourceImpl` | `FirebaseFirestore` |
| `FirebaseFavoriteFirestoreSource` | `FirebaseFavoriteFirestoreSourceImpl` | `FirebaseFirestore` |
| `FirebaseHistoryFirestoreSource` | `FirebaseHistoryFirestoreSourceImpl` | `FirebaseFirestore` |

---

## Dependency Graph

```
ApiModule
  IsoDateTimeAdapter + KotlinJsonAdapterFactory
    └─ Moshi
  HttpLoggingInterceptor (BODY in DEBUG, NONE in RELEASE)
  NetworkInterceptor (@Inject constructor — auto-resolved by Hilt)
  OkHttpClient (NetworkInterceptor + HttpLoggingInterceptor, 30s connect/read/write)
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
  ChapterCacheDatabase(@ApplicationContext, CHAPTER_CACHE_DB_NAME, fallbackToDestructiveMigration=true)
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

**New repository:**
1. Create the impl in `data/repository/` with `@Inject constructor(...)`.
2. Add one `@Binds @Singleton` abstract fun to `RepositoryModule`.
3. Add the interface→impl binding to the table above.

**New Firebase source:**
1. Create the interface in `data/network/firebase/`.
2. Create the impl with `@Inject constructor(firestore: FirebaseFirestore)` (or `FirebaseAuth`).
3. Add a `@Provides @Singleton` fun to `FirebaseModule` taking the impl, returning the interface.
4. Update the source binding table above.

**New local data source (Room DAO or DataStore):**
1. If new DAO: add the DAO interface to `ChapterCacheDatabase` (or a new `@Database`), then add a `@Provides @Singleton` fun to `LocalModule` delegating to `db.newDao()`.
2. If new DataStore file: add the extension property in the relevant manager file, then add a `@Provides @Singleton` fun.

**New module file:** Only needed for a distinct new infrastructure concern (e.g., a second REST API). Create a new `object` module in `di/network/` following the `ApiModule` pattern.

---

## Key Configuration Details

- `BASE_URL` comes from `BuildConfig`, populated from `local.properties` at build time. `UPLOAD_URL` is also in `local.properties` but used directly in the data layer (not wired through DI).
- Moshi adapter order: `IsoDateTimeAdapter` → `KotlinJsonAdapterFactory`. Never swap this order.
- Room `fallbackToDestructiveMigration(true)`: safe only because chapter cache is re-fetchable. Do not use this strategy for user data (favorites, history) — those live in Firestore, not Room.
- `NetworkInterceptor` is auto-resolved by Hilt. It does not need an explicit `@Provides`. Adding one would cause a duplicate binding error.
