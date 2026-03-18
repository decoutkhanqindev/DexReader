package com.decoutkhanqindev.dexreader.presentation.screens.category_details


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.criteria.MangaSortCriteriaModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.MangaSortOrderModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaContentRatingModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaStatusModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CategoryDetailsCriteriaUiState(
  val sortCriteria: MangaSortCriteriaModel = MangaSortCriteriaModel.LATEST_UPDATE,
  val sortOrder: MangaSortOrderModel = MangaSortOrderModel.DESC,
  val statusFilter: ImmutableList<MangaStatusModel> = persistentListOf(MangaStatusModel.ON_GOING),
  val contentRatingFilter: ImmutableList<MangaContentRatingModel> = persistentListOf(MangaContentRatingModel.SAFE),
)
