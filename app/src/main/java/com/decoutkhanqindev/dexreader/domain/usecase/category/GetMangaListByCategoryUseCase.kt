package com.decoutkhanqindev.dexreader.domain.usecase.category

import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.repository.CategoryRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class GetMangaListByCategoryUseCase @Inject constructor(
  private val repository: CategoryRepository,
) {
  suspend operator fun invoke(
    categoryId: String,
    offset: Int = 0,
    // sorting
    lastUpdated: String? = null, // latest update
    followedCount: String? = null, // trending
    createdAt: String? = null, // new release
    rating: String? = null, // top rated
    // filters
    status: List<String> = listOf("ongoing"), // ongoing, completed, hiatus, cancelled
    contentRating: List<String> = listOf("safe"), // safe, suggestive, erotica
  ): Result<List<Manga>> = runSuspendResultCatching {
    repository.getMangaListByCategory(
      categoryId = categoryId,
      offset = offset,
      lastUpdated = lastUpdated,
      followedCount = followedCount,
      createdAt = createdAt,
      rating = rating,
      status = status,
      contentRating = contentRating
    )
  }
}