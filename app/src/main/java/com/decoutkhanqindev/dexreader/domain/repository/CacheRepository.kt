package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.ChapterPages

interface CacheRepository {
  suspend fun addChapterCache(mangaId: String, chapterPages: ChapterPages)
  suspend fun getChapterCache(chapterId: String): ChapterPages
  suspend fun deleteChapterCache(chapterId: String)
  suspend fun clearExpiredCache(expiryTimestamp: Long)
}