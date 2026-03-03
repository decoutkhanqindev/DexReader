package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.mapper.CategoryMapper.toCategory
import com.decoutkhanqindev.dexreader.data.mapper.MangaMapper.toManga
import com.decoutkhanqindev.dexreader.data.mapper.ParamMapper.toParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.MangaDexApiService
import com.decoutkhanqindev.dexreader.di.UploadUrlQualifier
import com.decoutkhanqindev.dexreader.domain.exception.RemoteException
import com.decoutkhanqindev.dexreader.domain.model.Category
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaContentRatingFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaStatusFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.repository.CategoryRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
  private val mangaDexApiService: MangaDexApiService,
  @param:UploadUrlQualifier private val uploadUrl: String,
) : CategoryRepository {
  override suspend fun getCategoryList(): List<Category> =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        mangaDexApiService.getTagList().data?.map { it.toCategory() } ?: emptyList()
      },
      onCatch = { e ->
        when (e) {
          is RemoteException -> throw e
          is HttpException -> throw RemoteException.RequestFailed(cause = e)
          is IOException -> throw RemoteException.NetworkUnavailable(cause = e)
          else -> throw e
        }
      }
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
      },
      onCatch = { e ->
        when (e) {
          is RemoteException -> throw e
          is HttpException -> throw RemoteException.RequestFailed(cause = e)
          is IOException -> throw RemoteException.NetworkUnavailable(cause = e)
          else -> throw e
        }
      }
    )
}
