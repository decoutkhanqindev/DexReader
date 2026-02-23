package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.local.database.dao.ChapterCacheDao
import com.decoutkhanqindev.dexreader.data.mapper.toDomain
import com.decoutkhanqindev.dexreader.data.mapper.toEntity
import com.decoutkhanqindev.dexreader.domain.model.ChapterPages
import com.decoutkhanqindev.dexreader.domain.repository.CacheRepository
import com.decoutkhanqindev.dexreader.utils.AsyncHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CacheRepositoryImpl @Inject constructor(
  private val chapterCacheDao: ChapterCacheDao,
) : CacheRepository {
  override suspend fun addChapterCache(
    mangaId: String,
    chapterPages: ChapterPages,
  ) = withContext(Dispatchers.IO) {
    chapterCacheDao.addChapterCache(chapterCacheEntity = chapterPages.toEntity(mangaId))
  }

  override suspend fun getChapterCache(chapterId: String): ChapterPages =
    withContext(Dispatchers.IO) {
      chapterCacheDao.getChapterCache(chapterId)?.toDomain()
        ?: throw IllegalArgumentException("Chapter cache not found.")
    }

  override suspend fun deleteChapterCache(chapterId: String) =
    withContext(Dispatchers.IO) {
      chapterCacheDao.deleteChapterCache(chapterId)
    }

  override suspend fun clearExpiredCache(expiryTimestamp: Long) =
    withContext(Dispatchers.IO) {
      chapterCacheDao.clearExpiredCache(expiryTimestamp)
    }
}
