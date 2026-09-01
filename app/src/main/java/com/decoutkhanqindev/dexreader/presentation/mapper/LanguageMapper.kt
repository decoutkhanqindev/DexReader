package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.presentation.model.value.language.AppLanguageValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaLanguageValue

object LanguageMapper {
  fun MangaLanguage.toMangaLanguageValue() =
    MangaLanguageValue.valueOf(this.name)

  fun MangaLanguageValue.toMangaLanguage() =
    MangaLanguage.valueOf(this.name)

  fun MangaLanguage.toAppLanguageValue(): AppLanguageValue =
    AppLanguageValue.entries.find { it.language.name == this.name } ?: AppLanguageValue.DEFAULT

  fun AppLanguageValue.toMangaLanguage(): MangaLanguage =
    MangaLanguage.valueOf(this.language.name)
}
