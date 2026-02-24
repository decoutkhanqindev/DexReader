package com.decoutkhanqindev.dexreader.domain.usecase.favorites

import com.decoutkhanqindev.dexreader.domain.repository.FavoritesRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class RemoveFromFavoritesUseCase @Inject constructor(
  private val repository: FavoritesRepository,
) {
  suspend operator fun invoke(userId: String, mangaId: String): Result<Unit> =
    runSuspendResultCatching {
      repository.removeFromFavorites(userId, mangaId)
    }
}