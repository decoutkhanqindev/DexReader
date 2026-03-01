package com.decoutkhanqindev.dexreader.domain.usecase.cache

import com.decoutkhanqindev.dexreader.domain.repository.CacheRepository
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
    const val CACHE_EXPIRY_MILLIS = 24 * 60 * 60 * 1000L // 24h
  }
}