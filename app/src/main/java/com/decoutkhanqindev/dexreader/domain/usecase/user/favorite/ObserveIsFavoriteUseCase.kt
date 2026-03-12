package com.decoutkhanqindev.dexreader.domain.usecase.user.favorite

import com.decoutkhanqindev.dexreader.domain.repository.user.FavoritesRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.toFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveIsFavoriteUseCase @Inject constructor(
  private val repository: FavoritesRepository,
) {
  operator fun invoke(userId: String, mangaId: String): Flow<Result<Boolean>> =
    repository.observeIsFavorite(userId, mangaId).toFlowResult()
}
