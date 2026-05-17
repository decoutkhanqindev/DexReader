package com.decoutkhanqindev.dexreader.presentation.model.value.criteria

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.R

@Immutable
enum class MangaSortOrderValue(@param:StringRes val nameRes: Int) {
  ASC(R.string.sort_order_ascending),
  DESC(R.string.sort_order_descending)
}
