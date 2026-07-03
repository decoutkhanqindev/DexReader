package com.decoutkhanqindev.dexreader.domain.usecase.manga

import com.decoutkhanqindev.dexreader.domain.entity.manga.Manga
import com.decoutkhanqindev.dexreader.domain.entity.manga.MangaStats
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaRepository
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaStatsRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class GetMangaDetailsUseCase @Inject constructor(
  private val mangaRepository: MangaRepository,
  private val statsRepository: MangaStatsRepository,
) {
  suspend operator fun invoke(mangaId: String): Result<Manga> =
    runSuspendResultCatching {
      val details: Manga = mangaRepository.getMangaDetails(mangaId)
      val stats: List<MangaStats> = statsRepository.getMangaStats(listOf(mangaId))
      Manga.mergeStats(listOf(details), stats).first()
    }
}
