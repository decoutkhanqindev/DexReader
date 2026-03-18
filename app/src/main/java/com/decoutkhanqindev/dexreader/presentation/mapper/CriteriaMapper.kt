package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.criteria.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.model.criteria.MangaSortOrder
import com.decoutkhanqindev.dexreader.presentation.model.criteria.MangaSortCriteriaUiModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.MangaSortOrderUiModel

object CriteriaMapper {

  fun MangaSortCriteriaUiModel.toMangaSortCriteria() =
    MangaSortCriteria.valueOf(this.name)

  fun MangaSortOrderUiModel.toMangaSortOrder() =
    MangaSortOrder.valueOf(this.name)
}
