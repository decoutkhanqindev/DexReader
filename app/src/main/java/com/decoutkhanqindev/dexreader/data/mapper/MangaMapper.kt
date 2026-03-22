package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toMangaContentRating
import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toMangaLanguage
import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toMangaStatus
import com.decoutkhanqindev.dexreader.data.mapper.CategoryMapper.toCategory
import com.decoutkhanqindev.dexreader.data.network.api.response.manga.MangaResponse
import com.decoutkhanqindev.dexreader.domain.entity.manga.Manga
import com.decoutkhanqindev.dexreader.util.TimeAgo.parseIso8601ToEpoch

object MangaMapper {

  private const val LANG_EN = "en"
  private const val REL_COVER_ART = "cover_art"
  private const val REL_AUTHOR = "author"
  private const val REL_ARTIST = "artist"
  private const val COVER_URL_SEGMENT = "covers"

  fun MangaResponse.toManga(uploadUrl: String): Manga {
    val title = attributes?.title?.get(LANG_EN)
      ?: attributes?.title?.values?.firstOrNull()
      ?: Manga.DEFAULT_TITLE
    val coverUrl = relationships?.find {
      it.type == REL_COVER_ART
    }?.attributes?.fileName?.let { fileName ->
      "$uploadUrl/$COVER_URL_SEGMENT/$id/$fileName"
    } ?: Manga.DEFAULT_COVER_URL
    val description =
      attributes?.description?.get(LANG_EN)
        ?: attributes?.description?.values?.firstOrNull()
    val author =
      relationships?.find { it.type == REL_AUTHOR }?.attributes?.name
    val artist =
      relationships?.find { it.type == REL_ARTIST }?.attributes?.name
    val tags = attributes?.tags?.map { it.toCategory() } ?: emptyList()
    val status = attributes?.status.toMangaStatus()
    val contentRating = attributes?.contentRating.toMangaContentRating()
    val year = attributes?.year?.toString()
    val availableLanguages =
      attributes?.availableTranslatedLanguages
        ?.map { it.toMangaLanguage() }
        ?: emptyList()
    val latestChapter = attributes?.lastChapter
    val updatedAt = attributes?.updatedAt.parseIso8601ToEpoch()

    return Manga(
      id = id,
      title = title,
      coverUrl = coverUrl,
      description = description,
      author = author,
      artist = artist,
      categories = tags,
      status = status,
      contentRating = contentRating,
      year = year,
      availableLanguages = availableLanguages,
      latestChapter = latestChapter,
      updatedAt = updatedAt,
    )
  }
}
