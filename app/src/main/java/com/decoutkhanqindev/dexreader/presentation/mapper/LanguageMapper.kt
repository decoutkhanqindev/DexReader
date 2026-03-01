package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.MangaLanguage
import com.decoutkhanqindev.dexreader.presentation.model.MangaLanguageName

object LanguageMapper {
  fun MangaLanguage.toMangaLanguageName(): MangaLanguageName =
    MangaLanguageName.valueOf(this.name)

  fun MangaLanguageName.toMangaLanguage(): MangaLanguage =
    MangaLanguage.valueOf(this.name)
}
