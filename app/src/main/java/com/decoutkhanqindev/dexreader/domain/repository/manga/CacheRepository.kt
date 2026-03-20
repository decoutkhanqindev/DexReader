package com.decoutkhanqindev.dexreader.domain.repository.manga

import com.decoutkhanqindev.dexreader.domain.entity.manga.ChapterPages

interface CacheRepository {
  suspend fun addChapterCache(chapterPages: ChapterPages)
  suspend fun getChapterCache(chapterId: String): ChapterPages
  suspend fun deleteChapterCache(chapterId: String)
  suspend fun clearExpiredCache(expiryTimestamp: Long)
}
