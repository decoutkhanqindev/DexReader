package com.decoutkhanqindev.dexreader.presentation.model.criteria.sort

import androidx.annotation.StringRes
import com.decoutkhanqindev.dexreader.R

enum class MangaSortOrderOption(@param:StringRes val nameRes: Int) {
  DESC(R.string.sort_order_descending),
  ASC(R.string.sort_order_ascending)
}
