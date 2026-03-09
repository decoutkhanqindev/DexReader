package com.decoutkhanqindev.dexreader.presentation.mapper


import com.decoutkhanqindev.dexreader.domain.model.FavoriteManga
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.presentation.mapper.CategoryUiMapper.toCategoryUiModel
import com.decoutkhanqindev.dexreader.presentation.mapper.LanguageMapper.toMangaLanguageUiModel
import com.decoutkhanqindev.dexreader.presentation.model.MangaUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

object MangaUiMapper {
  fun Manga.toMangaUiModel(): MangaUiModel = MangaUiModel(
    id = id,
    title = title,
    coverUrl = coverUrl,
    description = description,
    author = author,
    artist = artist,
    categories = categories.map { it.toCategoryUiModel() }.toPersistentList(),
    status = status.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
    year = year,
    availableLanguages = availableLanguages.map { it.toMangaLanguageUiModel() }.toPersistentList(),
    latestChapter = latestChapter,
    updatedAt = updatedAt,
  )

  fun FavoriteManga.toMangaUiModel(): MangaUiModel = MangaUiModel(
    id = id,
    title = title,
    coverUrl = coverUrl,
    description = "",
    author = author,
    artist = "",
    categories = persistentListOf(),
    status = status.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
    year = "",
    availableLanguages = persistentListOf(),
    latestChapter = "",
    updatedAt = "",
  )
}
