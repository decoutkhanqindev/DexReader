import os
import re

replacements = [
    ("import com.decoutkhanqindev.dexreader.domain.repository.manga.chapter.ChapterRepository",
     "import com.decoutkhanqindev.dexreader.domain.repository.manga.ChapterRepository"),
    ("import com.decoutkhanqindev.dexreader.domain.repository.manga.cache.CacheRepository",
     "import com.decoutkhanqindev.dexreader.domain.repository.manga.CacheRepository"),
    ("import com.decoutkhanqindev.dexreader.domain.repository.user.favorite.FavoritesRepository",
     "import com.decoutkhanqindev.dexreader.domain.repository.user.FavoritesRepository"),
    ("import com.decoutkhanqindev.dexreader.domain.repository.user.history.HistoryRepository",
     "import com.decoutkhanqindev.dexreader.domain.repository.user.HistoryRepository"),
]

root = r"C:\Android Development\dex_reader\DexReader\app\src\main\java"
updated_files = []

for dirpath, dirnames, filenames in os.walk(root):
    for filename in filenames:
        if not filename.endswith(".kt"):
            continue
        filepath = os.path.join(dirpath, filename)
        with open(filepath, "r", encoding="utf-8") as f:
            content = f.read()
        new_content = content
        for old, new in replacements:
            new_content = re.sub(
                re.escape(old) + r"(?=\s*$)",
                new,
                new_content,
                flags=re.MULTILINE,
            )
        if new_content != content:
            with open(filepath, "w", encoding="utf-8", newline="") as f:
                f.write(new_content)
            updated_files.append(filepath.replace(root + "\\", ""))

print(f"Updated {len(updated_files)} files:")
for f in sorted(updated_files):
    print(f"  {f}")
