package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.mapper.ParamMapper.toMangaLanguage
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.param.MangaIncludesParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.chapter.ChapterResponse
import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.MangaLanguage
import com.decoutkhanqindev.dexreader.util.TimeAgo.toTimeAgo

object ChapterMapper {

  fun ChapterResponse.toChapter(): Chapter {
    val mangaId = relationships?.find { it?.type == MangaIncludesParam.MANGA.value }?.id ?: "0"
    val title = attributes?.title ?: "Untitled"
    val chapterNumber = attributes?.chapter ?: "0"
    val volume = attributes?.volume ?: "0"
    val publishAt = attributes?.publishAt.toTimeAgo()
    val translatedLanguage =
      attributes?.translatedLanguage?.toMangaLanguage() ?: MangaLanguage.ENGLISH

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
