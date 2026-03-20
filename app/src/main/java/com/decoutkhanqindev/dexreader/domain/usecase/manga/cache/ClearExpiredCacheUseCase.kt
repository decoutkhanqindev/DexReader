package com.decoutkhanqindev.dexreader.domain.usecase.manga.cache

import com.decoutkhanqindev.dexreader.domain.repository.manga.CacheRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class ClearExpiredCacheUseCase @Inject constructor(
  private val repository: CacheRepository,
) {
  /** Overridable in tests to supply a deterministic timestamp. */
  internal var clock: () -> Long = System::currentTimeMillis

  suspend operator fun invoke(): Result<Unit> =
    runSuspendResultCatching {
      val expiryTimestamp = clock() - CACHE_EXPIRY_MILLIS
      repository.clearExpiredCache(expiryTimestamp)
    }

  private companion object {
    private const val CACHE_EXPIRY_MILLIS = 24 * 60 * 60 * 1000L // 24h
  }
}
