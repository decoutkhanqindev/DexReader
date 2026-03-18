package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.manga.FavoriteManga
import com.decoutkhanqindev.dexreader.presentation.mapper.MangaMapper.toMangaStatusModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaContentRatingModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import kotlinx.collections.immutable.persistentListOf

object FavoriteMangaMapper {
  fun FavoriteManga.toMangaModel() =
    MangaModel(
      id = id,
      title = title,
      coverUrl = coverUrl,
      description = "",
      author = author,
      artist = "",
      categories = persistentListOf(),
      status = status.toMangaStatusModel(),
      contentRating = MangaContentRatingModel.UNKNOWN,
      year = "",
      availableLanguages = persistentListOf(),
      latestChapter = "",
      updatedAt = "",
    )
}
