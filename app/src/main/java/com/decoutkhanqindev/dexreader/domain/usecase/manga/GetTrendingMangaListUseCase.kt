package com.decoutkhanqindev.dexreader.domain.usecase.manga

import com.decoutkhanqindev.dexreader.domain.entity.manga.Manga
import com.decoutkhanqindev.dexreader.domain.entity.manga.MangaStats
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaRepository
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaStatsRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class GetTrendingMangaListUseCase @Inject constructor(
  private val repository: MangaRepository,
  private val statsRepository: MangaStatsRepository,
) {
  suspend operator fun invoke(): Result<List<Manga>> =
    runSuspendResultCatching {
      val list: List<Manga> = repository.getTrendingMangaList()
      val listIds: List<String> = list.map { it.id }
      val stats: List<MangaStats> = statsRepository.getMangaStats(listIds)
      Manga.mergeStats(list, stats)
    }
}
