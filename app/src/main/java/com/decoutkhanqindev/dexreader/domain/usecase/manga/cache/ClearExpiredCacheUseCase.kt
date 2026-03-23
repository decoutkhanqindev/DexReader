package com.decoutkhanqindev.dexreader.domain.usecase.manga.cache

import com.decoutkhanqindev.dexreader.domain.repository.manga.CacheRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class ClearExpiredCacheUseCase @Inject constructor(
  private val repository: CacheRepository,
) {
  suspend operator fun invoke(): Result<Unit> =
    runSuspendResultCatching {
      val expiryTimestamp = System.currentTimeMillis() - CACHE_EXPIRY_MILLIS
      repository.clearExpiredCache(expiryTimestamp)
    }

  private companion object {
    private const val CACHE_EXPIRY_MILLIS = 24L * 60 * 60 * 1000 // 24h
  }
}
