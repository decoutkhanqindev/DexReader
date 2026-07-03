package com.decoutkhanqindev.dexreader.data.repository.manga

import com.decoutkhanqindev.dexreader.data.mapper.MangaStatsMapper.toMangaStats
import com.decoutkhanqindev.dexreader.data.network.api.ApiService
import com.decoutkhanqindev.dexreader.domain.entity.manga.MangaStats
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaStatsRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class MangaStatsRepositoryImpl @Inject constructor(
  private val apiService: ApiService,
) : MangaStatsRepository {
  override suspend fun getMangaStats(ids: List<String>): List<MangaStats> =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        apiService.getMangaStatistics(ids).statistics
          ?.map { (id, entry) -> entry.toMangaStats(id) }
          ?: emptyList()
      },
      catch = { emptyList() }
    )
}
