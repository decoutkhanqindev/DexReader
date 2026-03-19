package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.value.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.presentation.value.manga.MangaLanguageValue

object LanguageMapper {
  fun MangaLanguage.toMangaLanguageEnum() =
    MangaLanguageValue.valueOf(this.name)

  fun MangaLanguageValue.toMangaLanguage() =
    MangaLanguage.valueOf(this.name)
}
