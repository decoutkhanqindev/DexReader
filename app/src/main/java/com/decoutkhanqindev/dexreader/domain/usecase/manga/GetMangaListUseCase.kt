package com.decoutkhanqindev.dexreader.domain.usecase.manga

import com.decoutkhanqindev.dexreader.domain.entity.manga.Manga
import com.decoutkhanqindev.dexreader.domain.entity.manga.MangaStats
import com.decoutkhanqindev.dexreader.domain.entity.value.criteria.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.entity.value.criteria.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaContentRating
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaStatus
import com.decoutkhanqindev.dexreader.domain.repository.category.CategoryRepository
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaStatsRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.runSuspendResultCatching
import javax.inject.Inject

class GetMangaListUseCase @Inject constructor(
  private val categoryRepository: CategoryRepository,
  private val statsRepository: MangaStatsRepository,
) {
  suspend operator fun invoke(
    categoryId: String? = null,
    limit: Int = 20,
    offset: Int = 0,
    sortCriteria: MangaSortCriteria = MangaSortCriteria.LATEST_UPDATE,
    sortOrder: MangaSortOrder = MangaSortOrder.DESC,
    statusFilter: List<MangaStatus> = listOf(MangaStatus.ON_GOING),
    contentRatingFilter: List<MangaContentRating> = listOf(MangaContentRating.SAFE),
    includeStats: Boolean = true,
  ): Result<List<Manga>> = runSuspendResultCatching {
    val list: List<Manga> = categoryRepository.getMangaList(
      categoryId = categoryId,
      limit = limit,
      offset = offset,
      sortCriteria = sortCriteria,
      sortOrder = sortOrder,
      statusFilter = statusFilter,
      contentRatingFilter = contentRatingFilter,
    )
    if (!includeStats) {
      list
    } else {
      val listIds: List<String> = list.map { it.id }
      val stats: List<MangaStats> = statsRepository.getMangaStats(listIds)
      Manga.mergeStats(list, stats)
    }
  }
}
