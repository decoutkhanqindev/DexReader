package com.decoutkhanqindev.dexreader.presentation.screens.category_details


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.criteria.MangaSortCriteriaUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.MangaSortOrderUiModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaContentRatingUiModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaStatusUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CategoryDetailsCriteriaUiState(
  val sortCriteria: MangaSortCriteriaUiModel = MangaSortCriteriaUiModel.LATEST_UPDATE,
  val sortOrder: MangaSortOrderUiModel = MangaSortOrderUiModel.DESC,
  val statusFilter: ImmutableList<MangaStatusUiModel> = persistentListOf(MangaStatusUiModel.ON_GOING),
  val contentRatingFilter: ImmutableList<MangaContentRatingUiModel> = persistentListOf(MangaContentRatingUiModel.SAFE),
)
