package com.decoutkhanqindev.dexreader.presentation.screens.category_details


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortCriteriaValue
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortOrderValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CategoryDetailsCriteriaUiState(
  val sortCriteria: MangaSortCriteriaValue = MangaSortCriteriaValue.LATEST_UPDATE,
  val sortOrder: MangaSortOrderValue = MangaSortOrderValue.DESC,
  val statusFilter: ImmutableList<MangaStatusValue> = persistentListOf(MangaStatusValue.ON_GOING),
  val contentRatingFilter: ImmutableList<MangaContentRatingValue> = persistentListOf(
    MangaContentRatingValue.SAFE
  ),
)
