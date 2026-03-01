package com.decoutkhanqindev.dexreader.presentation.model.criteria.filter

import androidx.annotation.StringRes
import com.decoutkhanqindev.dexreader.R

enum class MangaStatusFilterOption(@param:StringRes val nameRes: Int) {
  ON_GOING(R.string.status_on_going),
  COMPLETED(R.string.status_completed),
  HIATUS(R.string.status_hiatus),
  CANCELLED(R.string.status_cancelled)
}
