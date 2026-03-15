package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.manga.FavoriteManga
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaUiModel
import kotlinx.collections.immutable.persistentListOf

object FavoriteMangaUiMapper {
  fun FavoriteManga.toMangaUiModel() =
    MangaUiModel(
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
