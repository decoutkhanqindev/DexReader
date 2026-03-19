package com.decoutkhanqindev.dexreader.domain.usecase.category

import com.decoutkhanqindev.dexreader.domain.entity.manga.Manga
import com.decoutkhanqindev.dexreader.domain.repository.category.CategoryRepository
import com.decoutkhanqindev.dexreader.domain.value.criteria.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.value.criteria.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.value.manga.MangaContentRating
import com.decoutkhanqindev.dexreader.domain.value.manga.MangaStatus
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
