package com.decoutkhanqindev.dexreader.domain.usecase.user.favorite

import com.decoutkhanqindev.dexreader.domain.repository.user.FavoritesRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.runSuspendResultCatching
import javax.inject.Inject

class RemoveFromFavoritesUseCase @Inject constructor(
  private val repository: FavoritesRepository,
) {
  suspend operator fun invoke(userId: String, mangaId: String): Result<Unit> =
    runSuspendResultCatching { repository.removeFromFavorites(userId, mangaId) }
}
