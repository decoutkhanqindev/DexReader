package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.mapper.ApiParamMapper.toParam
import com.decoutkhanqindev.dexreader.data.mapper.CategoryMapper.toCategory
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toDomainException
import com.decoutkhanqindev.dexreader.data.mapper.MangaMapper.toManga
import com.decoutkhanqindev.dexreader.data.network.api.ApiService
import com.decoutkhanqindev.dexreader.di.UploadUrlQualifier
import com.decoutkhanqindev.dexreader.domain.model.category.Category
import com.decoutkhanqindev.dexreader.domain.model.manga.Manga
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaContentRatingFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaStatusFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.repository.category.CategoryRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CategoryRepositoryImpl
@Inject
constructor(
  private val apiService: ApiService,
  @param:UploadUrlQualifier private val uploadUrl: String,
) : CategoryRepository {
  override suspend fun getCategoryList(): List<Category> =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        apiService.getTagList().data?.map { it.toCategory() } ?: emptyList()
      },
      onCatch = { it.toDomainException() }
    )

  override suspend fun getMangaListByCategory(
    categoryId: String,
    offset: Int,
    sortCriteria: MangaSortCriteria,
    sortOrder: MangaSortOrder,
    statusFilter: List<MangaStatusFilter>,
    contentRatingFilter: List<MangaContentRatingFilter>,
  ): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        val orderValue = sortOrder.toParam()
        apiService.getMangaListByTag(
          tagId = categoryId,
          offset = offset,
          lastUpdated =
            if (sortCriteria == MangaSortCriteria.LATEST_UPDATE) orderValue
            else null,
          followedCount =
            if (sortCriteria == MangaSortCriteria.TRENDING) orderValue
            else null,
          createdAt =
            if (sortCriteria == MangaSortCriteria.MOST_VIEWED) orderValue
            else null,
          rating =
            if (sortCriteria == MangaSortCriteria.TOP_RATED) orderValue
            else null,
          status = statusFilter.map { it.toParam() },
          contentRating = contentRatingFilter.map { it.toParam() },
        )
          .data
          ?.map { it.toManga(uploadUrl) }
          ?: emptyList()
      },
      onCatch = { it.toDomainException() }
    )
}
