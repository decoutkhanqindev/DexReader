package com.decoutkhanqindev.dexreader.presentation.model.error

import androidx.annotation.StringRes
import com.decoutkhanqindev.dexreader.R

sealed class FeatureError(@param:StringRes val messageRes: Int) {
  data object Network : FeatureError(R.string.no_internet_connection)
  data object MangaNotFound : FeatureError(R.string.manga_not_available)
  data object ChapterNotFound : FeatureError(R.string.chapter_not_available)
  data object Generic : FeatureError(R.string.oops_something_went_wrong_please_try_again)
}
