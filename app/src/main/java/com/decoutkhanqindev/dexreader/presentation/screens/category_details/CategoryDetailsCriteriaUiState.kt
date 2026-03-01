package com.decoutkhanqindev.dexreader.presentation.screens.category_details

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaContentRatingFilterOption
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortCriteriaOption
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortOrderOption
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaStatusFilterOption

@Immutable
data class CategoryDetailsCriteriaUiState(
  val sortCriteria: MangaSortCriteriaOption = MangaSortCriteriaOption.LATEST_UPDATE,
  val sortOrder: MangaSortOrderOption = MangaSortOrderOption.DESC,
  val statusFilter: List<MangaStatusFilterOption> = listOf(MangaStatusFilterOption.ON_GOING),
  val contentRatingFilter: List<MangaContentRatingFilterOption> = listOf(MangaContentRatingFilterOption.SAFE),
)
