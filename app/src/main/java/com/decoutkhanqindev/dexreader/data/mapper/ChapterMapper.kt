package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toMangaLanguage
import com.decoutkhanqindev.dexreader.data.network.api.response.chapter.ChapterResponse
import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.model.manga.Chapter
import com.decoutkhanqindev.dexreader.util.TimeAgo.toTimeAgo

object ChapterMapper {

  private const val REL_MANGA = "manga"

  fun ChapterResponse.toChapter(): Chapter {
    val mangaId = relationships?.find {
      it?.type == REL_MANGA
    }?.id ?: throw BusinessException.Resource.ChapterNotFound()
    val title = attributes?.title ?: Chapter.DEFAULT_TITLE
    val number = attributes?.chapter ?: Chapter.DEFAULT_CHAPTER_NUMBER
    val volume = attributes?.volume ?: Chapter.DEFAULT_VOLUME
    val publishedAt = attributes?.publishAt.toTimeAgo()
    val language =
      attributes?.translatedLanguage?.toMangaLanguage() ?: Chapter.DEFAULT_LANGUAGE

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
