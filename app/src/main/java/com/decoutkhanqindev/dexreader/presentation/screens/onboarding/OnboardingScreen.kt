package com.decoutkhanqindev.dexreader.presentation.screens.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.onboarding.OnboardingViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.onboarding.components.OnboardingContent
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateClearStack

@Composable
fun OnboardingScreen(
  navController: NavHostController,
  onboardingViewModel: OnboardingViewModel,
  modifier: Modifier = Modifier,
) {
  BackHandler { }

  OnboardingContent(
    modifier = modifier.background(MaterialTheme.colorScheme.background),
    onGetStartedClick = {
      onboardingViewModel.completeOnboarding()
      navController.navigateClearStack<NavRoute.Onboarding>(NavRoute.Main)
    },
  )
}
