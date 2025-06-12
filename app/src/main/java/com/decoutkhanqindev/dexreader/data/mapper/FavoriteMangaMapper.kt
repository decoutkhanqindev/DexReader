package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.FavoriteMangaDto
import com.decoutkhanqindev.dexreader.domain.model.FavoriteManga
import com.decoutkhanqindev.dexreader.utils.toTimeAgo

fun FavoriteMangaDto.toDomain() =
  FavoriteManga(
    id = id,
    title = title,
    coverUrl = coverUrl,
    author = author,
    status = status,
    addedAt = createAt?.time?.toTimeAgo() ?: "Unknown time"
  )

fun FavoriteManga.toDto() =
  FavoriteMangaDto(
    id = id,
    title = title,
    coverUrl = coverUrl,
    author = author,
    status = status,
  )