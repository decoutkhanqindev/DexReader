package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.Category
import com.decoutkhanqindev.dexreader.domain.model.Manga

interface CategoryRepository {
  suspend fun getCategoryList(): Result<List<Category>>
  suspend fun getMangaListByCategory(
    categoryId: String,
    offset: Int = 0,
    // sorting
    lastUpdated: String? = null, // latest update
    followedCount: String? = null, // trending
    createdAt: String? = null, // new release
    rating: String? = null, // top rated
    // filters
    status: String = "ongoing", // ongoing, completed, hiatus, cancelled
    contentRating: String = "safe", // safe, suggestive, erotica
  ): Result<List<Manga>>
}