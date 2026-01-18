package com.decoutkhanqindev.dexreader.domain.usecase.favorites

import com.decoutkhanqindev.dexreader.domain.repository.FavoritesRepository
import javax.inject.Inject

class RemoveFromFavoritesUseCase @Inject constructor(
  private val favoriteRepository: FavoritesRepository,
) {
  suspend operator fun invoke(userId: String, mangaId: String): Result<Unit> =
    favoriteRepository.removeFromFavorites(userId, mangaId)
}