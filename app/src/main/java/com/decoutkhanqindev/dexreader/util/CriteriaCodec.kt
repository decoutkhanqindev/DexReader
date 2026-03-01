package com.decoutkhanqindev.dexreader.util

import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaContentRatingFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.filter.MangaStatusFilter
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.FilterValue
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.SortCriteria
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.SortOrder

object CriteriaCodec {

  fun MangaSortCriteria.toSortCriteriaId(): String = when (this) {
    MangaSortCriteria.LATEST_UPDATE -> SortCriteria.LatestUpdate.id
    MangaSortCriteria.TRENDING      -> SortCriteria.Trending.id
    MangaSortCriteria.MOST_VIEWED   -> SortCriteria.NewReleases.id
    MangaSortCriteria.TOP_RATED     -> SortCriteria.TopRated.id
  }

  fun MangaSortOrder.toSortOrderId(): String = when (this) {
    MangaSortOrder.ASC  -> SortOrder.Ascending.id
    MangaSortOrder.DESC -> SortOrder.Descending.id
  }

  fun MangaStatusFilter.toFilterValueId(): String = when (this) {
    MangaStatusFilter.ON_GOING   -> FilterValue.Ongoing.id
    MangaStatusFilter.COMPLETED  -> FilterValue.Completed.id
    MangaStatusFilter.HIATUS     -> FilterValue.Hiatus.id
    MangaStatusFilter.CANCELLED  -> FilterValue.Cancelled.id
  }

  fun MangaContentRatingFilter.toFilterValueId(): String = when (this) {
    MangaContentRatingFilter.SAFE       -> FilterValue.Safe.id
    MangaContentRatingFilter.SUGGESTIVE -> FilterValue.Suggestive.id
    MangaContentRatingFilter.EROTICA    -> FilterValue.Erotica.id
  }
}
