package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.manga.FavoriteManga
import com.decoutkhanqindev.dexreader.presentation.mapper.MangaUiMapper.toMangaStatusUiModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaContentRatingUiModel
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
      status = status.toMangaStatusUiModel(),
      contentRating = MangaContentRatingUiModel.UNKNOWN,
      year = "",
      availableLanguages = persistentListOf(),
      latestChapter = "",
      updatedAt = "",
    )
}
