package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.dto.MangaDto
import com.decoutkhanqindev.dexreader.domain.model.Manga

fun MangaDto.toDomain(uploadUrl: String): Manga {
  val title = attributes.title["en"]
    ?: attributes.title.values.firstOrNull()
    ?: "Unknown Title"
  val coverUrl =
    relationships?.find { it.type == "cover_art" }.let { coverArt ->
      "$uploadUrl/covers/${id}/${coverArt?.attributes?.get("fileName")}"
    }
  val description = attributes.description?.get("en")
    ?: attributes.description?.values?.firstOrNull()
    ?: "No description available"
  val authorId = relationships?.find { it.type == "author" }?.id
  val artistId = relationships?.find { it.type == "artist" }?.id
  val genres = attributes.tags?.mapNotNull {
    it.attributes.name["en"]
  } ?: emptyList()
  val status = attributes.status ?: "Unknown Status"
  val lastChapter = attributes.lastChapter ?: "Updating ..."

  return Manga(
    id = id,
    title = title,
    coverUrl = coverUrl,
    description = description,
    author = authorId,
    artist = artistId,
    genres = genres,
    status = status,
    lastChapter = lastChapter
  )
}
