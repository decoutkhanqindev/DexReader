package com.decoutkhanqindev.dexreader.presentation.model.error

import androidx.annotation.StringRes
import com.decoutkhanqindev.dexreader.R

sealed class FeatureUiError(@param:StringRes val messageRes: Int) {
  data object NetworkUnavailable : FeatureUiError(R.string.no_internet_connection)
  data object ServerUnavailable : FeatureUiError(R.string.error_server_unavailable)
  data object AccessDenied : FeatureUiError(R.string.error_access_denied)
  data object MangaNotFound : FeatureUiError(R.string.manga_not_available)
  data object ChapterNotFound : FeatureUiError(R.string.chapter_not_available)
  data object Generic : FeatureUiError(R.string.oops_something_went_wrong_please_try_again)
}
