package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.api.response.statistics.MangaStatisticsEntryResponse
import com.decoutkhanqindev.dexreader.domain.entity.manga.MangaStats

object MangaStatsMapper {
  fun MangaStatisticsEntryResponse.toMangaStats(mangaId: String): MangaStats =
    MangaStats(
      mangaId = mangaId,
      rating = rating?.bayesian,
      follows = follows
    )
}
