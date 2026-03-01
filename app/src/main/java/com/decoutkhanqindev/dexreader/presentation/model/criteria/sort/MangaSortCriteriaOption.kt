package com.decoutkhanqindev.dexreader.presentation.model.criteria.sort

import androidx.annotation.StringRes
import com.decoutkhanqindev.dexreader.R

enum class MangaSortCriteriaOption(@param:StringRes val nameRes: Int) {
  LATEST_UPDATE(R.string.latest_update),
  TRENDING(R.string.trending),
  MOST_VIEWED(R.string.new_releases),
  TOP_RATED(R.string.top_rated)
}
