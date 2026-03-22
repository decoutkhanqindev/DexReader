package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.entity.manga.FavoriteManga
import com.decoutkhanqindev.dexreader.presentation.mapper.MangaMapper.toMangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.model.manga.FavoriteMangaModel

object FavoriteMangaMapper {
  fun FavoriteManga.toFavoriteMangaModel() =
    FavoriteMangaModel(
      id = id,
      title = title,
      coverUrl = coverUrl,
      author = author,
      status = status.toMangaStatusValue(),
    )
}
