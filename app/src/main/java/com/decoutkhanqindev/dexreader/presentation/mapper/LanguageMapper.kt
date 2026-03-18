package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaLanguageModel

object LanguageMapper {
  fun MangaLanguage.toMangaLanguageModel() =
    MangaLanguageModel.valueOf(this.name)

  fun MangaLanguageModel.toMangaLanguage() =
    MangaLanguage.valueOf(this.name)
}
