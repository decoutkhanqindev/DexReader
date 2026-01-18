package com.decoutkhanqindev.dexreader.domain.usecase.cache

import com.decoutkhanqindev.dexreader.domain.repository.CacheRepository
import javax.inject.Inject

class ClearExpiredCacheUseCase @Inject constructor(
  private val cacheRepository: CacheRepository,
) {
  suspend operator fun invoke(expiryTimestamp: Long): Result<Unit> =
    cacheRepository.clearExpiredCache(expiryTimestamp)
}