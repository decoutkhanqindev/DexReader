package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.entity.value.criteria.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.entity.value.criteria.MangaSortOrder
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortCriteriaValue
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortOrderValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaSectionValue

object CriteriaMapper {

  fun MangaSortCriteriaValue.toMangaSortCriteria() =
    MangaSortCriteria.valueOf(this.name)

  fun MangaSortOrderValue.toMangaSortOrder() =
    MangaSortOrder.valueOf(this.name)

  fun MangaSectionValue.toSortCriteriaValue(): MangaSortCriteriaValue =
    when (this) {
      MangaSectionValue.TRENDING -> MangaSortCriteriaValue.TRENDING
      MangaSectionValue.LATEST_UPDATE -> MangaSortCriteriaValue.LATEST_UPDATE
      MangaSectionValue.NEW_RELEASE -> MangaSortCriteriaValue.MOST_VIEWED
      MangaSectionValue.TOP_RATED -> MangaSortCriteriaValue.TOP_RATED
    }
}
