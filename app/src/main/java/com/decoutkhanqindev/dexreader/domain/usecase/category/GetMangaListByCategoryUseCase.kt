package com.decoutkhanqindev.dexreader.domain.usecase.category

import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.repository.CategoryRepository
import javax.inject.Inject

class GetMangaListByCategoryUseCase @Inject constructor(
  private val categoryRepository: CategoryRepository
) {
  suspend operator fun invoke(
    categoryId: String,
    offset: Int = 0,
    // sorting
    lastUpdated: String = "desc", // latest update
    followedCount: String = "desc", // trending
    createdAt: String = "desc", // new release
    rating: String = "desc", // top rated
    // filters
    status: String = "ongoing", // ongoing, completed, hiatus, cancelled
    contentRating: String = "safe", // safe, suggestive, erotica
  ): Result<List<Manga>> =
    categoryRepository.getMangaListByCategory(
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