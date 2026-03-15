package com.decoutkhanqindev.dexreader.presentation.mapper


import com.decoutkhanqindev.dexreader.domain.model.manga.Manga
import com.decoutkhanqindev.dexreader.presentation.mapper.CategoryUiMapper.toCategoryUiModel
import com.decoutkhanqindev.dexreader.presentation.mapper.LanguageMapper.toMangaLanguageUiModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaUiModel
import com.decoutkhanqindev.dexreader.util.TimeAgo.toTimeAgo
import kotlinx.collections.immutable.toPersistentList

object MangaUiMapper {
  fun Manga.toMangaUiModel() =
    MangaUiModel(
      id = id,
      title = title,
      coverUrl = coverUrl,
      description = description,
      author = author,
      artist = artist,
      categories = categories.map { it.toCategoryUiModel() }.toPersistentList(),
      status = status.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
      year = year,
      availableLanguages = availableLanguages.map { it.toMangaLanguageUiModel() }
        .toPersistentList(),
      latestChapter = latestChapter,
      updatedAt = updatedAt.toTimeAgo(),
    )

}
