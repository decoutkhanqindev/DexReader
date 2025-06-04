package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.ChapterPages

interface CacheRepository {
  suspend fun addChapterCache(mangaId: String, chapterPages: ChapterPages): Result<Unit>
  suspend fun getChapterCache(chapterId: String): Result<ChapterPages>
  suspend fun deleteChapterCache(chapterId: String): Result<Unit>
  suspend fun clearExpiredCache(expiryTimestamp: Long): Result<Unit>
}