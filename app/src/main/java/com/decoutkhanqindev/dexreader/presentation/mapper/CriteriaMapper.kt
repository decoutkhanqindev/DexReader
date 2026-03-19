package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.value.criteria.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.value.criteria.MangaSortOrder
import com.decoutkhanqindev.dexreader.presentation.value.criteria.MangaSortCriteriaValue
import com.decoutkhanqindev.dexreader.presentation.value.criteria.MangaSortOrderValue

object CriteriaMapper {

  fun MangaSortCriteriaValue.toMangaSortCriteria() =
    MangaSortCriteria.valueOf(this.name)

  fun MangaSortOrderValue.toMangaSortOrder() =
    MangaSortOrder.valueOf(this.name)
}
