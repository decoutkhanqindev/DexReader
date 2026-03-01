package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.mapper.CategoryMapper.toCategory
import com.decoutkhanqindev.dexreader.data.mapper.ParamMapper.toMangaLanguage
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.constant.MangaDexApiEndpoints
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.param.MangaIncludesParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.param.MangaLanguageCodeParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.manga.MangaResponse
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.util.TimeAgo.toTimeAgo

object MangaMapper {

  fun MangaResponse.toManga(uploadUrl: String): Manga {
    val title = attributes?.title[MangaLanguageCodeParam.ENGLISH.value]
      ?: attributes?.title?.values?.firstOrNull()
      ?: "Untitled"
    val coverUrl =
      relationships?.find { it.type == MangaIncludesParam.COVER_ART.value }.let { coverArt ->
        "$uploadUrl/${MangaDexApiEndpoints.COVER_PATH}/${id}/${coverArt?.attributes?.fileName}"
      }
    val description =
      attributes?.description?.get(MangaLanguageCodeParam.ENGLISH.value)
        ?: attributes?.description?.values?.firstOrNull()
        ?: "No description ..."
    val authorId =
      relationships?.find { it.type == MangaIncludesParam.AUTHOR.value }?.attributes?.name
        ?: "Unknown"
    val artistId =
      relationships?.find { it.type == MangaIncludesParam.ARTIST.value }?.attributes?.name
        ?: "Unknown"
    val tags = attributes?.tags?.map { it.toCategory() } ?: emptyList()
    val status = attributes?.status ?: "Unknown"
    val year = attributes?.year ?: "Unknown"
    val availableTranslatedLanguages =
      attributes?.availableTranslatedLanguages
        ?.map { it.toMangaLanguage() }
        ?: emptyList()
    val lastChapter = attributes?.lastChapter ?: "Unknown"
    val lastUpdated = attributes?.updatedAt.toTimeAgo()

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
}
