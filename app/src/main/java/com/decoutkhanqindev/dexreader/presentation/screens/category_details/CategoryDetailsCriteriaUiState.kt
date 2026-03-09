package com.decoutkhanqindev.dexreader.presentation.screens.category_details


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaContentRatingFilterUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaStatusFilterUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortCriteriaUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortOrderUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CategoryDetailsCriteriaUiState(
  val sortCriteria: MangaSortCriteriaUiModel = MangaSortCriteriaUiModel.LATEST_UPDATE,
  val sortOrder: MangaSortOrderUiModel = MangaSortOrderUiModel.DESC,
  val statusFilter: ImmutableList<MangaStatusFilterUiModel> = persistentListOf(
    MangaStatusFilterUiModel.ON_GOING
  ),
  val contentRatingFilter: ImmutableList<MangaContentRatingFilterUiModel> =
    persistentListOf(MangaContentRatingFilterUiModel.SAFE),
)
