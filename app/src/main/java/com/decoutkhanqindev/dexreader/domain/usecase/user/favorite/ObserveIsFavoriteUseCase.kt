package com.decoutkhanqindev.dexreader.domain.usecase.user.favorite

import com.decoutkhanqindev.dexreader.domain.repository.user.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveIsFavoriteUseCase @Inject constructor(
  private val repository: FavoritesRepository,
) {
  operator fun invoke(userId: String, mangaId: String): Flow<Boolean> =
    repository.observeIsFavorite(userId, mangaId)
}
