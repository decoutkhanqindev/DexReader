package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.value.criteria.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.value.criteria.MangaSortOrder
import com.decoutkhanqindev.dexreader.presentation.model.criteria.MangaSortCriteriaModel
import com.decoutkhanqindev.dexreader.presentation.model.criteria.MangaSortOrderModel

object CriteriaMapper {

  fun MangaSortCriteriaModel.toMangaSortCriteria() =
    MangaSortCriteria.valueOf(this.name)

  fun MangaSortOrderModel.toMangaSortOrder() =
    MangaSortOrder.valueOf(this.name)
}
