package com.decoutkhanqindev.dexreader.domain.usecase.favorites

import com.decoutkhanqindev.dexreader.domain.model.FavoriteManga
import com.decoutkhanqindev.dexreader.domain.repository.FavoritesRepository
import javax.inject.Inject

class AddToFavoritesUseCase @Inject constructor(
  private val favoriteRepository: FavoritesRepository
) {
  suspend operator fun invoke(userId: String, manga: FavoriteManga): Result<Unit> =
    favoriteRepository.addToFavorites(userId, manga)
}