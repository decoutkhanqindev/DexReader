package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.ChapterPages
import com.decoutkhanqindev.dexreader.domain.model.MangaLanguage
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder

interface ChapterRepository {
  suspend fun getChapterList(
    mangaId: String,
    limit: Int = 20,
    offset: Int = 0,
    language: MangaLanguage = MangaLanguage.ENGLISH,
    sortOrder: MangaSortOrder = MangaSortOrder.DESC,
  ): List<Chapter>

  suspend fun getChapterDetails(chapterId: String): Chapter
  suspend fun getChapterPages(chapterId: String): ChapterPages
}
