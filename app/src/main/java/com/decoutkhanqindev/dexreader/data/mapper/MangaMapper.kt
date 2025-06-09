package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.MangaDto
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
  val authorId = relationships?.find { it.type == "author" }?.attributes?.name ?: "Unknown"
  val artistId = relationships?.find { it.type == "artist" }?.attributes?.name ?: "Unknown"
  val tags = attributes.tags?.mapNotNull { it.toDomain() } ?: emptyList()
  val status = attributes.status ?: "Unknown"
  val year = attributes.year ?: "Unknown"
  val availableTranslatedLanguages = attributes.availableTranslatedLanguages
    ?.filterNotNull()
    ?.map { it.toFullLanguageName() }
    ?: emptyList()
  val lastChapter = attributes.lastChapter ?: "Unknown"
  val lastUpdated = attributes.updatedAt.toTimeAgo()

  return Manga(
    id = id,
    title = title,
    coverUrl = coverUrl,
    description = description,
    author = authorId,
    artist = artistId,
    categories = tags,
    status = status,
    year = year,
    availableTranslatedLanguages = availableTranslatedLanguages,
    lastChapter = lastChapter,
    lastUpdated = lastUpdated,
  )
}
