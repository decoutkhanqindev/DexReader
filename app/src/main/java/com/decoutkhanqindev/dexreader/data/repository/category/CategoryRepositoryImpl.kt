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
import com.decoutkhanqindev.dexreader.domain.repository.settings.SettingsRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
  private val apiService: ApiService,
  private val settingsRepository: SettingsRepository,
) : CategoryRepository {
  override suspend fun getCategoryList(): List<Category> =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        val preferredLanguage = settingsRepository.observeContentLanguage().first()
        apiService.getTagList().data?.mapNotNull {
          it.toCategory(preferredLanguage = preferredLanguage)
        } ?: emptyList()
      },
      catch = { it.toDomainException() }
    )

  override suspend fun getMangaList(
    categoryId: String?,
    limit: Int,
    offset: Int,
    sortCriteria: MangaSortCriteria,
    sortOrder: MangaSortOrder,
    statusFilter: List<MangaStatus>,
    contentRatingFilter: List<MangaContentRating>,
  ): List<Manga> =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        val preferredLanguage = settingsRepository.observeContentLanguage().first()
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

        apiService.getMangaList(
          limit = limit,
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
          ?.mapNotNull { it.toManga(BuildConfig.UPLOAD_URL, preferredLanguage) }
          ?: emptyList()
      },
      catch = { it.toDomainException() }
    )
}
