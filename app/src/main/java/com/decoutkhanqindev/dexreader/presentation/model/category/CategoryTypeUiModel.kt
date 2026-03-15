package com.decoutkhanqindev.dexreader.presentation.model.category

import androidx.annotation.StringRes
import com.decoutkhanqindev.dexreader.R

enum class CategoryTypeUiModel(@param:StringRes val nameRes: Int) {
  GENRE(R.string.category_type_genre),
  THEME(R.string.category_type_theme),
  FORMAT(R.string.category_type_format),
  CONTENT(R.string.category_type_content),
  UNKNOWN(R.string.category_type_unknown)
}
