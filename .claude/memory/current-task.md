# Current Task

## Status
All DI qualifier removal complete. No pending implementation work.

## Last Actions (this session)
1. Split `NetworkDataModule.kt` into `di/network/ApiModule.kt` + `di/network/FirebaseModule.kt` — COMPLETE
2. Removed all 4 qualifiers across the `di/` directory — COMPLETE:
   - `@BaseUrlQualifier` — removed from ApiModule; `BASE_URL` inlined into `provideRetrofit()`
   - `@UploadUrlQualifier` — removed; repos now use `BuildConfig.UPLOAD_URL` directly
   - `@MangaDexApiServiceQualifier` — removed; only one Retrofit binding
   - `@ThemeModeKeyQualifier` — removed; `THEME_MODE_KEY = "theme_mode"` companion const in SettingsRepositoryImpl

## What's Next
- Run `./gradlew assembleDebug` to verify clean build (not yet run — no Bash access during batch)
- No pending implementation work otherwise
