package com.decoutkhanqindev.dexreader.domain.repository.manga

import com.decoutkhanqindev.dexreader.domain.entity.manga.MangaStats

interface MangaStatsRepository {
  suspend fun getMangaStats(ids: List<String>): List<MangaStats>
}
