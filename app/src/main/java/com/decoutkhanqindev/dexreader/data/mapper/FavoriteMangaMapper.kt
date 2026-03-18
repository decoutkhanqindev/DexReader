package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toApiValue
import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toMangaStatus
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.request.FavoriteMangaRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.FavoriteMangaResponse
import com.decoutkhanqindev.dexreader.domain.model.manga.FavoriteManga

object FavoriteMangaMapper {

  fun FavoriteMangaResponse.toFavoriteManga() =
    FavoriteManga(
      id = id,
      title = title,
      coverUrl = coverUrl,
      author = author,
      status = status.toMangaStatus(),
      addedAt = createdAt?.time
    )

  fun FavoriteManga.toFavoriteMangaRequest() =
    FavoriteMangaRequest(
      id = id,
      title = title,
      coverUrl = coverUrl,
      author = author,
      status = status.toApiValue(),
    )
}
