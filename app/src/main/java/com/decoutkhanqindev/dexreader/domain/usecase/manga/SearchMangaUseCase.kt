package com.decoutkhanqindev.dexreader.domain.usecase.manga

import com.decoutkhanqindev.dexreader.domain.entity.manga.Manga
import com.decoutkhanqindev.dexreader.domain.entity.manga.MangaStats
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaRepository
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaStatsRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class SearchMangaUseCase @Inject constructor(
  private val mangaRepository: MangaRepository,
  private val statsRepository: MangaStatsRepository,
) {
  suspend operator fun invoke(
    query: String,
    offset: Int = 0,
    limit: Int = 20,
  ): Result<List<Manga>> =
    runSuspendResultCatching {
      val list: List<Manga> = mangaRepository.searchManga(
        query = query,
        offset = offset,
        limit = limit
      )
      val listIds: List<String> = list.map { it.id }
      val stats: List<MangaStats> = statsRepository.getMangaStats(listIds)
      Manga.mergeStats(list, stats)
    }
}
