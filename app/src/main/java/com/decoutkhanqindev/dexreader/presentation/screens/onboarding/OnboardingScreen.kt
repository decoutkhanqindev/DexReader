package com.decoutkhanqindev.dexreader.presentation.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.onboarding.OnboardingViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.onboarding.components.OnboardingContent

@Composable
fun OnboardingScreen(
  viewModel: OnboardingViewModel,
  modifier: Modifier = Modifier,
  onNavigateToMainScreen: () -> Unit,
) {
  OnboardingContent(
    modifier = modifier.background(MaterialTheme.colorScheme.background),
    onGetStartedClick = {
      viewModel.completeOnboarding()
      onNavigateToMainScreen()
    },
  )
}
