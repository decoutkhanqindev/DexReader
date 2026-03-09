package com.decoutkhanqindev.dexreader.presentation.screens.category_details

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaContentRatingFilterUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaStatusFilterUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortCriteriaUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortOrderUiModel

@Immutable
data class CategoryDetailsCriteriaUiState(
  val sortCriteria: MangaSortCriteriaUiModel = MangaSortCriteriaUiModel.LATEST_UPDATE,
  val sortOrder: MangaSortOrderUiModel = MangaSortOrderUiModel.DESC,
  val statusFilter: List<MangaStatusFilterUiModel> = listOf(MangaStatusFilterUiModel.ON_GOING),
  val contentRatingFilter: List<MangaContentRatingFilterUiModel> =
    listOf(MangaContentRatingFilterUiModel.SAFE),
)
