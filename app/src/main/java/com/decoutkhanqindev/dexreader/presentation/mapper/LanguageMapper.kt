package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.MangaLanguage
import com.decoutkhanqindev.dexreader.presentation.model.MangaLanguageUiModel

object LanguageMapper {
  fun MangaLanguage.toMangaLanguageUiModel(): MangaLanguageUiModel =
    MangaLanguageUiModel.valueOf(this.name)

  fun MangaLanguageUiModel.toMangaLanguage(): MangaLanguage =
    MangaLanguage.valueOf(this.name)
}
