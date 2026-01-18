package com.decoutkhanqindev.dexreader.domain.usecase.favorites

import com.decoutkhanqindev.dexreader.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveIsFavoriteUseCase @Inject constructor(
  private val favoriteRepository: FavoritesRepository,
) {
  operator fun invoke(userId: String, mangaId: String): Flow<Result<Boolean>> =
    favoriteRepository.observeIsFavorite(userId, mangaId)
}