package com.decoutkhanqindev.dexreader.domain.repository.category

import com.decoutkhanqindev.dexreader.domain.entity.category.Category
import com.decoutkhanqindev.dexreader.domain.entity.manga.Manga
import com.decoutkhanqindev.dexreader.domain.entity.value.criteria.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.entity.value.criteria.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaContentRating
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaStatus

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
