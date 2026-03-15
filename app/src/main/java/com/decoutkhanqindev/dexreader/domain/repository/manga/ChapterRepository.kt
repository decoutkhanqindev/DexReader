package com.decoutkhanqindev.dexreader.domain.repository.manga

import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.model.manga.Chapter
import com.decoutkhanqindev.dexreader.domain.model.manga.ChapterPages
import com.decoutkhanqindev.dexreader.domain.model.manga.MangaLanguage

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
