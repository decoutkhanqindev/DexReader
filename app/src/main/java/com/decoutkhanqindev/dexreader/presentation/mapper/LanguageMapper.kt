package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageValue

object LanguageMapper {
  fun MangaLanguage.toLanguageValue() =
    LanguageValue.valueOf(this.name)

  fun LanguageValue.toMangaLanguage() =
    MangaLanguage.valueOf(this.name)
}
