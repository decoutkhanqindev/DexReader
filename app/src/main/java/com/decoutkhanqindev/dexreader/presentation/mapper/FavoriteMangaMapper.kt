package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.entity.manga.FavoriteManga
import com.decoutkhanqindev.dexreader.presentation.mapper.MangaMapper.toMangaStatusEnum
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.value.manga.MangaContentRatingValue
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
      status = status.toMangaStatusEnum(),
      contentRating = MangaContentRatingValue.UNKNOWN,
      year = "",
      availableLanguages = persistentListOf(),
      latestChapter = "",
      updatedAt = "",
    )
}
