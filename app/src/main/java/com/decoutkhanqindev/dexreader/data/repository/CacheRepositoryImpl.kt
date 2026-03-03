package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.local.database.dao.ChapterCacheDao
import com.decoutkhanqindev.dexreader.data.mapper.ChapterPagesMapper.toChapterCacheEntity
import com.decoutkhanqindev.dexreader.data.mapper.ChapterPagesMapper.toChapterPages
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toCacheDomainException
import com.decoutkhanqindev.dexreader.domain.exception.MangaException
import com.decoutkhanqindev.dexreader.domain.model.ChapterPages
import com.decoutkhanqindev.dexreader.domain.repository.CacheRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CacheRepositoryImpl
@Inject
constructor(
  private val chapterCacheDao: ChapterCacheDao,
) : CacheRepository {
  override suspend fun addChapterCache(
    mangaId: String,
    chapterPages: ChapterPages,
  ) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        chapterCacheDao.addChapterCache(
          chapterCacheEntity = chapterPages.toChapterCacheEntity(mangaId)
        )
      },
      onCatch = { it.toCacheDomainException() }
    )

  override suspend fun getChapterCache(chapterId: String): ChapterPages =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        chapterCacheDao.getChapterCache(chapterId)?.toChapterPages()
          ?: throw MangaException.ChapterDataNotFound()
      },
      onCatch = { it.toCacheDomainException() }
    )

  override suspend fun deleteChapterCache(chapterId: String) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = { chapterCacheDao.deleteChapterCache(chapterId) },
      onCatch = { it.toCacheDomainException() }
    )

  override suspend fun clearExpiredCache(expiryTimestamp: Long) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = { chapterCacheDao.clearExpiredCache(expiryTimestamp) },
      onCatch = { it.toCacheDomainException() }
    )
}
