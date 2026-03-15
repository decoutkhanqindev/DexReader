package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaContentRatingFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaStatusFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaContentRatingFilterUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaStatusFilterUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortCriteriaUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortOrderUiModel

object CriteriaMapper {

  fun MangaSortCriteriaUiModel.toMangaSortCriteria() =
    MangaSortCriteria.valueOf(this.name)

  fun MangaSortOrderUiModel.toMangaSortOrder() =
    MangaSortOrder.valueOf(this.name)

  fun MangaStatusFilterUiModel.toMangaStatusFilter() =
    MangaStatusFilter.valueOf(this.name)

  fun MangaContentRatingFilterUiModel.toMangaContentRatingFilter() =
    MangaContentRatingFilter.valueOf(this.name)
}
