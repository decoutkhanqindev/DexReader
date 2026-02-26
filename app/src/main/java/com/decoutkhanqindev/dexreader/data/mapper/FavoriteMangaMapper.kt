package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.firebase.request.FavoriteMangaRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.response.FavoriteMangaResponse
import com.decoutkhanqindev.dexreader.domain.model.FavoriteManga
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.util.TimeAgo.toTimeAgo

fun FavoriteMangaResponse.toFavoriteManga() =
  FavoriteManga(
    id = id,
    title = title,
    coverUrl = coverUrl,
    author = author,
    status = status,
    addedAt = createdAt?.time?.toTimeAgo() ?: "Unknown time"
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
    availableTranslatedLanguages = emptyList(),
    lastChapter = "",
    lastUpdated = ""
  )
