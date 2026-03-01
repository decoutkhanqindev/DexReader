package com.decoutkhanqindev.dexreader.presentation.model.criteria.filter

import androidx.annotation.StringRes
import com.decoutkhanqindev.dexreader.R

enum class MangaContentRatingFilterOption(@param:StringRes val nameRes: Int) {
  SAFE(R.string.content_rating_safe),
  SUGGESTIVE(R.string.content_rating_suggestive),
  EROTICA(R.string.content_rating_erotica)
}
