package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.entity.manga.FavoriteManga
import com.decoutkhanqindev.dexreader.presentation.mapper.MangaMapper.toMangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.model.manga.FavoriteMangaModel
import com.decoutkhanqindev.dexreader.util.DataTypeFormatter.toFormattedCount
import java.util.Locale

object FavoriteMangaMapper {
  fun FavoriteManga.toFavoriteMangaModel() =
    FavoriteMangaModel(
      id = id,
      title = title,
      coverUrl = coverUrl,
      author = author,
      status = status.toMangaStatusValue(),
      rating = rating?.let { String.format(Locale.US, "%.1f", it) } ?: "",
      follows = follows?.toFormattedCount() ?: "",
    )
}
