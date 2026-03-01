package com.decoutkhanqindev.dexreader.domain.usecase.category

import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaContentRatingFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaStatusFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.repository.CategoryRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class GetMangaListByCategoryUseCase @Inject constructor(
  private val repository: CategoryRepository,
) {
  suspend operator fun invoke(
    categoryId: String,
    offset: Int = 0,
    sortCriteria: MangaSortCriteria = MangaSortCriteria.LATEST_UPDATE,
    sortOrder: MangaSortOrder = MangaSortOrder.DESC,
    statusFilter: List<MangaStatusFilter> = listOf(MangaStatusFilter.ON_GOING),
    contentRatingFilter: List<MangaContentRatingFilter> = listOf(MangaContentRatingFilter.SAFE),
  ): Result<List<Manga>> = runSuspendResultCatching {
    repository.getMangaListByCategory(
      categoryId = categoryId,
      offset = offset,
      sortCriteria = sortCriteria,
      sortOrder = sortOrder,
      statusFilter = statusFilter,
      contentRatingFilter = contentRatingFilter,
    )
  }
}
