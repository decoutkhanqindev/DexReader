package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.mapper.toDomain
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.MangaDexApiService
import com.decoutkhanqindev.dexreader.di.UploadUrlQualifier
import com.decoutkhanqindev.dexreader.domain.model.Category
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.repository.CategoryRepository
import com.decoutkhanqindev.dexreader.utils.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
  private val mangaDexApiService: MangaDexApiService,
  @UploadUrlQualifier private val uploadUrl: String
) : CategoryRepository {
  override suspend fun getCategoryList(): Result<List<Category>> =
    runSuspendCatching(Dispatchers.IO) {
      mangaDexApiService.getTagList().data.map { it.toDomain() }
    }

  override suspend fun getMangaListByCategory(
    categoryId: String,
    offset: Int,
    // sorting
    lastUpdated: String, // latest update
    followedCount: String, // trending
    createdAt: String, // new release
    rating: String, // top rated
    // filters
    status: String, // ongoing, completed, hiatus, cancelled
    contentRating: String, // safe, suggestive, erotica
  ): Result<List<Manga>> =
    runSuspendCatching(Dispatchers.IO) {
      mangaDexApiService.getMangaListByTag(
        tagId = categoryId,
        offset = offset,
        lastUpdated = lastUpdated,
        followedCount = followedCount,
        createdAt = createdAt,
        rating = rating,
        status = status,
        contentRating = contentRating
      ).data.map { it.toDomain(uploadUrl) }
    }
}