package com.decoutkhanqindev.dexreader.domain.usecase.category

import com.decoutkhanqindev.dexreader.domain.model.manga.MangaContentRating
import com.decoutkhanqindev.dexreader.domain.model.manga.MangaStatus
import com.decoutkhanqindev.dexreader.domain.model.criteria.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.model.criteria.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.model.manga.Manga
import com.decoutkhanqindev.dexreader.domain.repository.category.CategoryRepository
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
    statusFilter: List<MangaStatus> = listOf(MangaStatus.ON_GOING),
    contentRatingFilter: List<MangaContentRating> = listOf(MangaContentRating.SAFE),
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
