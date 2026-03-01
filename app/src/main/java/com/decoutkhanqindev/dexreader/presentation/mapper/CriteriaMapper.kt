package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaContentRatingFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaStatusFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaContentRatingFilterOption
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortCriteriaOption
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortOrderOption
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaStatusFilterOption

object CriteriaMapper {

  fun MangaSortCriteriaOption.toMangaSortCriteria(): MangaSortCriteria =
    MangaSortCriteria.valueOf(this.name)

  fun MangaSortOrderOption.toMangaSortOrder(): MangaSortOrder =
    MangaSortOrder.valueOf(this.name)

  fun MangaStatusFilterOption.toMangaStatusFilter(): MangaStatusFilter =
    MangaStatusFilter.valueOf(this.name)

  fun MangaContentRatingFilterOption.toMangaContentRatingFilter(): MangaContentRatingFilter =
    MangaContentRatingFilter.valueOf(this.name)
}
