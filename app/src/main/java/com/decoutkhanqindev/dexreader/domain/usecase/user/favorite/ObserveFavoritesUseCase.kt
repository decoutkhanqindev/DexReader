package com.decoutkhanqindev.dexreader.domain.usecase.user.favorite

import com.decoutkhanqindev.dexreader.domain.entity.manga.FavoriteManga
import com.decoutkhanqindev.dexreader.domain.entity.manga.MangaStats
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaStatsRepository
import com.decoutkhanqindev.dexreader.domain.repository.user.FavoritesRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.toFlowResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveFavoritesUseCase @Inject constructor(
  private val favoritesRepository: FavoritesRepository,
  private val statsRepository: MangaStatsRepository,
) {
  operator fun invoke(
    userId: String,
    limit: Int = 20,
    lastFavoriteMangaId: String? = null,
  ): Flow<Result<List<FavoriteManga>>> =
    favoritesRepository.observeFavorites(
      userId = userId,
      limit = limit,
      lastFavoriteMangaId = lastFavoriteMangaId
    ).map { list: List<FavoriteManga> ->
      val listIds: List<String> = list.map { it.id }
      val stats: List<MangaStats> = statsRepository.getMangaStats(listIds)
      FavoriteManga.mergeStats(list, stats)
    }.toFlowResult()
}
