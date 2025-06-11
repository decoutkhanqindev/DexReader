package com.decoutkhanqindev.dexreader.domain.usecase.favorite

import com.decoutkhanqindev.dexreader.domain.model.FavoriteManga
import com.decoutkhanqindev.dexreader.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoritesUseCase @Inject constructor(
  private val favoriteRepository: FavoritesRepository
) {
  operator fun invoke(userId: String): Flow<Result<List<FavoriteManga>>> =
    favoriteRepository.observeFavorites(userId)
}