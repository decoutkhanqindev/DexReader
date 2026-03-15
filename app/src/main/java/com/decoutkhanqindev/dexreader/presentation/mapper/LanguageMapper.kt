package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaLanguageUiModel

object LanguageMapper {
  fun MangaLanguage.toMangaLanguageUiModel() =
    MangaLanguageUiModel.valueOf(this.name)

  fun MangaLanguageUiModel.toMangaLanguage() =
    MangaLanguage.valueOf(this.name)
}
