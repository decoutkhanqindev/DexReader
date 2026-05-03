package com.decoutkhanqindev.dexreader.presentation.error

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.R

@Immutable
sealed class FeatureError(@param:StringRes val messageRes: Int) {
  @Immutable
  data object NetworkUnavailable : FeatureError(R.string.no_internet_connection)

  @Immutable
  data object ServerUnavailable : FeatureError(R.string.error_server_unavailable)

  @Immutable
  data object AccessDenied : FeatureError(R.string.error_access_denied)

  @Immutable
  data object MangaNotFound : FeatureError(R.string.manga_not_available)

  @Immutable
  data object ChapterNotFound : FeatureError(R.string.chapter_not_available)

  @Immutable
  data object Generic : FeatureError(R.string.oops_something_went_wrong_please_try_again)
}
