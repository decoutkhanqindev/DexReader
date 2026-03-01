package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.request.FavoriteMangaRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.FavoriteMangaResponse
import com.decoutkhanqindev.dexreader.domain.model.FavoriteManga
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.util.TimeAgo.toTimeAgo

object FavoriteMangaMapper {

  fun FavoriteMangaResponse.toFavoriteManga() =
    FavoriteManga(
      id = id,
      title = title,
      coverUrl = coverUrl,
      author = author,
      status = status,
      addedAt = createdAt?.time?.toTimeAgo() ?: FavoriteManga.DEFAULT_ADDED_AT
    )

  fun FavoriteManga.toFavoriteMangaRequest() =
    FavoriteMangaRequest(
      id = id,
      title = title,
      coverUrl = coverUrl,
      author = author,
      status = status,
    )

  fun FavoriteManga.toManga() =
    Manga(
      id = id,
      title = title,
      coverUrl = coverUrl,
      description = "",
      author = author,
      artist = "",
      categories = emptyList(),
      status = status,
      year = "",
      availableLanguages = emptyList(),
      latestChapter = "",
      updatedAt = ""
    )
}
