package com.decoutkhanqindev.dexreader.data.repository.manga

import com.decoutkhanqindev.dexreader.data.local.database.dao.ChapterCacheDao
import com.decoutkhanqindev.dexreader.data.mapper.ChapterPagesMapper.toChapterCacheEntity
import com.decoutkhanqindev.dexreader.data.mapper.ChapterPagesMapper.toChapterPages
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toUnexpectedException
import com.decoutkhanqindev.dexreader.domain.entity.manga.ChapterPages
import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.repository.manga.CacheRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CacheRepositoryImpl @Inject constructor(
  private val cacheDao: ChapterCacheDao,
) : CacheRepository {
  override suspend fun addChapterCache(chapterPages: ChapterPages) =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        cacheDao.addChapterCache(
          chapterCacheEntity = chapterPages.toChapterCacheEntity()
        )
      },
      catch = { it.toUnexpectedException() }
    )

  override suspend fun getChapterCache(chapterId: String): ChapterPages =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        cacheDao.getChapterCache(chapterId)?.toChapterPages()
          ?: throw BusinessException.Resource.ChapterDataNotFound()
      },
      catch = { it.toUnexpectedException() }
    )

  override suspend fun deleteChapterCache(chapterId: String) =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = { cacheDao.deleteChapterCache(chapterId) },
      catch = { it.toUnexpectedException() }
    )

  override suspend fun clearExpiredCache(olderThan: Long) =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = { cacheDao.clearExpiredCache(olderThan) },
      catch = { it.toUnexpectedException() }
    )
}
