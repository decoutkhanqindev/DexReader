package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.ChapterPages

interface ChapterRepository {
  suspend fun getChapterList(
    mangaId: String,
    limit: Int = 20,
    offset: Int = 0,
    translatedLanguage: String = "en",
    volumeOrder: String = "desc",
    chapterOrder: String = "desc"
  ): Result<List<Chapter>>

  suspend fun getChapterDetails(chapterId: String): Result<Chapter>
  suspend fun getChapterPages(chapterId: String): Result<ChapterPages>
}