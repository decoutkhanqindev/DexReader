package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toMangaLanguage
import com.decoutkhanqindev.dexreader.data.network.api.response.chapter.ChapterResponse
import com.decoutkhanqindev.dexreader.domain.entity.manga.Chapter
object ChapterMapper {

  private const val REL_MANGA = "manga"

  fun ChapterResponse.toChapter(): Chapter? {
    val mangaId = relationships?.find {
      it?.type == REL_MANGA
    }?.id ?: return null
    val title = attributes?.title
    val number = attributes?.chapter
    val volume = attributes?.volume
    val publishedAt = attributes?.publishAt
    val language = attributes?.translatedLanguage.toMangaLanguage()

    return Chapter(
      id = id,
      mangaId = mangaId,
      title = title,
      number = number,
      volume = volume,
      publishedAt = publishedAt,
      language = language
    )
  }
}
