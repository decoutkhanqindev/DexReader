package com.decoutkhanqindev.dexreader.presentation.model.value.onboarding

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
sealed interface OnboardingPagerItem {
  @Immutable
  data class Content(val page: OnboardingPageValue) : OnboardingPagerItem

  @Immutable
  data object FullScreenAd : OnboardingPagerItem

  companion object {
    val entries: ImmutableList<OnboardingPagerItem> = persistentListOf(
      Content(OnboardingPageValue.DISCOVER),
      Content(OnboardingPageValue.BROWSE),
      FullScreenAd,
      Content(OnboardingPageValue.READ),
      Content(OnboardingPageValue.TRACK),
    )
  }
}
