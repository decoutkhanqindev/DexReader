package com.decoutkhanqindev.dexreader.presentation.model.value.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.R

@Immutable
enum class OnboardingPageValue(
  @param:DrawableRes val imageRes: Int,
  @param:StringRes val titleRes: Int,
  @param:StringRes val descriptionRes: Int,
) {
  DISCOVER(
    imageRes = R.drawable.ob_discover,
    titleRes = R.string.onboarding_discover_title,
    descriptionRes = R.string.onboarding_discover_description,
  ),
  BROWSE(
    imageRes = R.drawable.ob_browse,
    titleRes = R.string.onboarding_browse_title,
    descriptionRes = R.string.onboarding_browse_description,
  ),
  READ(
    imageRes = R.drawable.ob_read,
    titleRes = R.string.onboarding_read_title,
    descriptionRes = R.string.onboarding_read_description,
  ),
  TRACK(
    imageRes = R.drawable.ob_track,
    titleRes = R.string.onboarding_track_title,
    descriptionRes = R.string.onboarding_track_description,
  ),
}
