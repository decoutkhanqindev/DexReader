package com.decoutkhanqindev.dexreader.presentation.screens.category_details

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaContentRatingFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaStatusFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder

@Immutable
data class CategoryDetailsCriteriaUiState(
  val sortCriteria: MangaSortCriteria = MangaSortCriteria.LATEST_UPDATE,
  val sortOrder: MangaSortOrder = MangaSortOrder.DESC,
  val statusFilter: List<MangaStatusFilter> = listOf(MangaStatusFilter.ON_GOING),
  val contentRatingFilter: List<MangaContentRatingFilter> = listOf(MangaContentRatingFilter.SAFE),
)
