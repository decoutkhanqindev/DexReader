package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.Category
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaContentRatingFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaStatusFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder

interface CategoryRepository {
  suspend fun getCategoryList(): List<Category>
  suspend fun getMangaListByCategory(
    categoryId: String,
    offset: Int = 0,
    sortCriteria: MangaSortCriteria = MangaSortCriteria.LATEST_UPDATE,
    sortOrder: MangaSortOrder = MangaSortOrder.DESC,
    statusFilter: List<MangaStatusFilter> = listOf(MangaStatusFilter.ON_GOING),
    contentRatingFilter: List<MangaContentRatingFilter> = listOf(MangaContentRatingFilter.SAFE),
  ): List<Manga>
}
