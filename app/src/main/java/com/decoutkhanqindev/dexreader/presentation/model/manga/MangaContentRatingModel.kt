package com.decoutkhanqindev.dexreader.presentation.model.manga

import androidx.annotation.StringRes
import com.decoutkhanqindev.dexreader.R

enum class MangaContentRatingModel(@param:StringRes val nameRes: Int) {
  SAFE(R.string.content_rating_safe),
  SUGGESTIVE(R.string.content_rating_suggestive),
  EROTICA(R.string.content_rating_erotica),
  UNKNOWN(R.string.content_rating_unknown)
}
