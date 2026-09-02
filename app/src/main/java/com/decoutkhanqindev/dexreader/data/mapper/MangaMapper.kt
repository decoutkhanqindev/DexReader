package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toApiParam
import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toMangaContentRating
import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toMangaLanguage
import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toMangaStatus
import com.decoutkhanqindev.dexreader.data.mapper.CategoryMapper.toCategory
import com.decoutkhanqindev.dexreader.data.mapper.LocalizedTextMapper.localized
import com.decoutkhanqindev.dexreader.data.network.api.response.manga.MangaResponse
import com.decoutkhanqindev.dexreader.domain.entity.manga.Manga
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.util.DataTypeFormatter.parseIso8601ToEpoch

object MangaMapper {

  private const val REL_COVER_ART = "cover_art"
  private const val REL_AUTHOR = "author"
  private const val REL_ARTIST = "artist"
  private const val COVER_URL_SEGMENT = "covers"

  fun MangaResponse.toManga(
    uploadUrl: String,
    preferredLanguage: MangaLanguage = MangaLanguage.ENGLISH,
  ): Manga? {
    val resolvedId = id ?: return null
    val languageCode = preferredLanguage.toApiParam()

    val title = attributes?.title
      .localized(languageCode = languageCode, altTexts = attributes?.altTitles)
      ?: Manga.DEFAULT_TITLE

    val coverUrl = relationships?.find {
      it.type == REL_COVER_ART
    }?.attributes?.fileName?.let { fileName ->
      "$uploadUrl/$COVER_URL_SEGMENT/$resolvedId/$fileName"
    } ?: Manga.DEFAULT_COVER_URL

    val description = attributes?.description.localized(languageCode)
    val author =
      relationships?.find { it.type == REL_AUTHOR }?.attributes?.name
    val artist =
      relationships?.find { it.type == REL_ARTIST }?.attributes?.name
    val tags = attributes?.tags?.mapNotNull {
      it.toCategory(preferredLanguage = preferredLanguage)
    } ?: emptyList()
    val status = attributes?.status.toMangaStatus()
    val contentRating = attributes?.contentRating.toMangaContentRating()
    val year = attributes?.year?.toString()

    val availableLanguages =
      attributes?.availableTranslatedLanguages
        ?.map { it.toMangaLanguage() }
        ?.distinct()
        ?: emptyList()

    val latestChapter = attributes?.lastChapter
    val updatedAt = attributes?.updatedAt.parseIso8601ToEpoch()

    return Manga(
      id = resolvedId,
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
      rating = null,
      follows = null,
    )
  }
}
