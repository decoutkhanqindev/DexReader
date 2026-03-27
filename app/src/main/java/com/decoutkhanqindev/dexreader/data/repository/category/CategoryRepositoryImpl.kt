package com.decoutkhanqindev.dexreader.data.repository.category

import com.decoutkhanqindev.dexreader.BuildConfig
import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toApiParam
import com.decoutkhanqindev.dexreader.data.mapper.CategoryMapper.toCategory
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toDomainException
import com.decoutkhanqindev.dexreader.data.mapper.MangaMapper.toManga
import com.decoutkhanqindev.dexreader.data.network.api.ApiService
import com.decoutkhanqindev.dexreader.domain.entity.category.Category
import com.decoutkhanqindev.dexreader.domain.entity.manga.Manga
import com.decoutkhanqindev.dexreader.domain.entity.value.criteria.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.entity.value.criteria.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaContentRating
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaStatus
import com.decoutkhanqindev.dexreader.domain.repository.category.CategoryRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
  private val apiService: ApiService,
) : CategoryRepository {
  override suspend fun getCategoryList(): List<Category> =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        apiService.getTagList().data?.mapNotNull { it.toCategory() } ?: emptyList()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun getMangaListByCategory(
    categoryId: String,
    offset: Int,
    sortCriteria: MangaSortCriteria,
    sortOrder: MangaSortOrder,
    statusFilter: List<MangaStatus>,
    contentRatingFilter: List<MangaContentRating>,
  ): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        val orderValue = sortOrder.toApiParam()
        val lastUpdated: String?
        val followedCount: String?
        val createdAt: String?
        val rating: String?

        when (sortCriteria) {
          MangaSortCriteria.LATEST_UPDATE -> {
            lastUpdated = orderValue
            followedCount = null
            createdAt = null
            rating = null
          }

          MangaSortCriteria.TRENDING -> {
            lastUpdated = null
            followedCount = orderValue
            createdAt = null
            rating = null
          }

          MangaSortCriteria.MOST_VIEWED -> {
            lastUpdated = null
            followedCount = null
            createdAt = orderValue
            rating = null
          }

          MangaSortCriteria.TOP_RATED -> {
            lastUpdated = null
            followedCount = null
            createdAt = null
            rating = orderValue
          }
        }

        apiService.getMangaListByTag(
          tagId = categoryId,
          offset = offset,
          lastUpdated = lastUpdated,
          followedCount = followedCount,
          createdAt = createdAt,
          rating = rating,
          status = statusFilter
            .filter { it != MangaStatus.UNKNOWN }
            .map { it.toApiParam() },
          contentRating = contentRatingFilter
            .filter { it != MangaContentRating.UNKNOWN }
            .map { it.toApiParam() },
        )
          .data
          ?.mapNotNull { it.toManga(BuildConfig.UPLOAD_URL) }
          ?: emptyList()
      },
      catch = { it.toDomainException() }
    )
}
