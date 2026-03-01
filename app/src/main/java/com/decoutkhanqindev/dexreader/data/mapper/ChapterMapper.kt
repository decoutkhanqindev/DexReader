package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.mapper.ParamMapper.toMangaLanguage
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.param.MangaIncludesParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.chapter.ChapterResponse
import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.util.TimeAgo.toTimeAgo

object ChapterMapper {

  fun ChapterResponse.toChapter(): Chapter {
    val mangaId = relationships?.find { it?.type == MangaIncludesParam.MANGA.value }?.id ?: Chapter.DEFAULT_MANGA_ID
    val title = attributes?.title ?: Chapter.DEFAULT_TITLE
    val chapterNumber = attributes?.chapter ?: Chapter.DEFAULT_CHAPTER_NUMBER
    val volume = attributes?.volume ?: Chapter.DEFAULT_VOLUME
    val publishAt = attributes?.publishAt.toTimeAgo()
    val translatedLanguage =
      attributes?.translatedLanguage?.toMangaLanguage() ?: Chapter.DEFAULT_LANGUAGE

    return Chapter(
      id = id,
      mangaId = mangaId,
      title = title,
      chapterNumber = chapterNumber,
      volume = volume,
      publishAt = publishAt,
      translatedLanguage = translatedLanguage
    )
  }
}
