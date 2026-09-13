package com.decoutkhanqindev.dexreader.data.repository.manga

import com.decoutkhanqindev.dexreader.data.local.database.dao.ChapterCacheDao
import com.decoutkhanqindev.dexreader.data.mapper.ChapterPagesMapper.toChapterCacheEntity
import com.decoutkhanqindev.dexreader.data.mapper.ChapterPagesMapper.toChapterPages
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toUnexpectedException
import com.decoutkhanqindev.dexreader.domain.entity.manga.ChapterPages
import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.repository.manga.CacheRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.withContextCatching
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CacheRepositoryImpl @Inject constructor(
  private val cacheDao: ChapterCacheDao,
) : CacheRepository {
  override suspend fun addChapterCache(chapterPages: ChapterPages) =
    withContextCatching(
      context = Dispatchers.IO,
      action = {
        cacheDao.addChapterCache(
          chapterCacheEntity = chapterPages.toChapterCacheEntity()
        )
      },
      catch = { it.toUnexpectedException() }
    )

  override suspend fun getChapterCache(chapterId: String): ChapterPages =
    withContextCatching(
      context = Dispatchers.IO,
      action = {
        cacheDao.getChapterCache(chapterId)?.toChapterPages()
          ?: throw BusinessException.Resource.ChapterDataNotFound()
      },
      catch = { it.toUnexpectedException() }
    )

  override suspend fun deleteChapterCache(chapterId: String) =
    withContextCatching(
      context = Dispatchers.IO,
      action = { cacheDao.deleteChapterCache(chapterId) },
      catch = { it.toUnexpectedException() }
    )

  override suspend fun clearExpiredCache(olderThan: Long) =
    withContextCatching(
      context = Dispatchers.IO,
      action = { cacheDao.clearExpiredCache(olderThan) },
      catch = { it.toUnexpectedException() }
    )
}
