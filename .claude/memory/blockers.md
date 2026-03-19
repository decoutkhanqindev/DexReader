# Blockers

## ACTIVE — Codebase is non-compiling (naming convention mismatch)

**Problem:**
`MangaModel.kt` was modified (externally/intentionally) to reference types that do not exist:

```kotlin
import com.decoutkhanqindev.dexreader.presentation.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.value.manga.MangaLanguageValue
import com.decoutkhanqindev.dexreader.presentation.value.manga.MangaStatusValue
```

These types don't exist — `presentation/value/` directory has not been created yet.

**Root cause:**
This session's migration used `presentation/enum/` + `*Enum` naming.
After migration completed, `MangaModel.kt` was externally updated to use `presentation/value/` +
`*Value`.
This created a split state:

- `MangaModel.kt` → `*Value` references (types do NOT exist)
- All other ~30+ consumer files → `*Enum` references (types DO exist in `presentation/enum/`)

**Required to unblock:**
Apply the `value/` + `*Value` naming consistently:

1. Create `presentation/value/` directory with 4 subdirs
2. Create 7 `*Value` files (copy content from `*Enum` files, rename package + class)
3. Bulk sed: replace `*Enum` → `*Value` and `presentation.enum` → `presentation.value` in all .kt
   files
4. Delete `presentation/enum/` directory
5. Verify MangaModel.kt is correct after sed (it starts in the right state already)
6. Run `./gradlew assembleDebug`

**Note:** The external edit to MangaModel.kt is intentional — do not revert it. It defines the
target state.
