package com.decoutkhanqindev.dexreader.domain.usecase.user.favorite

import com.decoutkhanqindev.dexreader.domain.repository.user.FavoritesRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.suspendRunCatching
import javax.inject.Inject

class RemoveFromFavoritesUseCase @Inject constructor(
  private val repository: FavoritesRepository,
) {
  suspend operator fun invoke(userId: String, mangaId: String): Result<Unit> =
    suspendRunCatching { repository.removeFromFavorites(userId, mangaId) }
}
