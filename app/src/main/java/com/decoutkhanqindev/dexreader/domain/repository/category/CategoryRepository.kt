package com.decoutkhanqindev.dexreader.domain.repository.category

import com.decoutkhanqindev.dexreader.domain.model.category.Category
import com.decoutkhanqindev.dexreader.domain.model.manga.MangaContentRating
import com.decoutkhanqindev.dexreader.domain.model.manga.MangaStatus
import com.decoutkhanqindev.dexreader.domain.model.criteria.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.model.criteria.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.model.manga.Manga

interface CategoryRepository {
  suspend fun getCategoryList(): List<Category>
  suspend fun getMangaListByCategory(
    categoryId: String,
    offset: Int = 0,
    sortCriteria: MangaSortCriteria = MangaSortCriteria.LATEST_UPDATE,
    sortOrder: MangaSortOrder = MangaSortOrder.DESC,
    statusFilter: List<MangaStatus> = listOf(MangaStatus.ON_GOING),
    contentRatingFilter: List<MangaContentRating> = listOf(MangaContentRating.SAFE),
  ): List<Manga>
}
