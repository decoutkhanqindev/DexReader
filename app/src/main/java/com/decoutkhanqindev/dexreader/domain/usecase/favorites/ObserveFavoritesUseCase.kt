package com.decoutkhanqindev.dexreader.domain.usecase.favorites

import com.decoutkhanqindev.dexreader.domain.model.FavoriteManga
import com.decoutkhanqindev.dexreader.domain.repository.FavoritesRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.toFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoritesUseCase @Inject constructor(
  private val repository: FavoritesRepository,
) {
  operator fun invoke(
    userId: String,
    limit: Int = 20,
    lastFavoriteMangaId: String? = null,
  ): Flow<Result<List<FavoriteManga>>> =
    repository.observeFavorites(
      userId = userId,
      limit = limit,
      lastFavoriteMangaId = lastFavoriteMangaId
    ).toFlowResult()
}