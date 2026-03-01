package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.mapper.toCategory
import com.decoutkhanqindev.dexreader.data.mapper.toManga
import com.decoutkhanqindev.dexreader.data.mapper.toParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.MangaDexApiService
import com.decoutkhanqindev.dexreader.di.UploadUrlQualifier
import com.decoutkhanqindev.dexreader.domain.model.Category
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaContentRatingFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaStatusFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
  private val mangaDexApiService: MangaDexApiService,
  @param:UploadUrlQualifier private val uploadUrl: String,
) : CategoryRepository {
  override suspend fun getCategoryList(): List<Category> =
    withContext(Dispatchers.IO) {
      mangaDexApiService.getTagList().data?.map { it.toCategory() } ?: emptyList()
    }

  override suspend fun getMangaListByCategory(
    categoryId: String,
    offset: Int,
    sortCriteria: MangaSortCriteria,
    sortOrder: MangaSortOrder,
    statusFilter: List<MangaStatusFilter>,
    contentRatingFilter: List<MangaContentRatingFilter>,
  ): List<Manga> = withContext(Dispatchers.IO) {
    val orderValue = sortOrder.toParam()
    mangaDexApiService.getMangaListByTag(
      tagId = categoryId,
      offset = offset,
      lastUpdated = if (sortCriteria == MangaSortCriteria.LATEST_UPDATE) orderValue else null,
      followedCount = if (sortCriteria == MangaSortCriteria.TRENDING) orderValue else null,
      createdAt = if (sortCriteria == MangaSortCriteria.MOST_VIEWED) orderValue else null,
      rating = if (sortCriteria == MangaSortCriteria.TOP_RATED) orderValue else null,
      status = statusFilter.map { it.toParam() },
      contentRating = contentRatingFilter.map { it.toParam() },
    ).data?.map { it.toManga(uploadUrl) } ?: emptyList()
  }
}
