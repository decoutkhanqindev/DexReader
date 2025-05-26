package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.dto.MangaDto
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.utils.toFullLanguageName
import com.decoutkhanqindev.dexreader.utils.toTimeAgo

fun MangaDto.toDomain(uploadUrl: String): Manga {
  val title = attributes.title["en"]
    ?: attributes.title.values.firstOrNull()
    ?: "Untitled"
  val coverUrl =
    relationships?.find { it.type == "cover_art" }.let { coverArt ->
      "$uploadUrl/covers/${id}/${coverArt?.attributes?.fileName}"
    }
  val description = attributes.description?.get("en")
    ?: attributes.description?.values?.firstOrNull()
    ?: "No description ..."
  val authorId = relationships?.find { it.type == "author" }?.attributes?.name ?: "Unknown author"
  val artistId = relationships?.find { it.type == "artist" }?.attributes?.name ?: "Unknown artist"
  val genres = attributes.tags?.mapNotNull { it.attributes.name["en"] } ?: emptyList()
  val status = attributes.status ?: "Unknown status"
  val year = attributes.year.toString()
  val availableTranslatedLanguages =
    attributes.availableTranslatedLanguages?.map { it.toFullLanguageName() } ?: emptyList()
  val lastChapter = attributes.lastChapter ?: "Unknown"
  val lastUpdated = attributes.updatedAt.toTimeAgo()

  return Manga(
    id = id,
    title = title,
    coverUrl = coverUrl,
    description = description,
    author = authorId,
    artist = artistId,
    genres = genres,
    status = status,
    year = year,
    availableTranslatedLanguages = availableTranslatedLanguages,
    lastChapter = lastChapter,
    lastUpdated = lastUpdated,
  )
}
