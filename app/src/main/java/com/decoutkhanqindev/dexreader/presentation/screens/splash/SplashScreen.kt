package com.decoutkhanqindev.dexreader.presentation.screens.splash

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.splash.components.SplashContent
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateClearStack
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  navController: NavHostController,
  isOnboardingCompleted: Boolean?,
  modifier: Modifier = Modifier,
) {
  val latestIsOnboardingCompleted by rememberUpdatedState(isOnboardingCompleted)
  val latestNavController by rememberUpdatedState(navController)

  LaunchedEffect(Unit) {
    delay(3000L)
    if (latestIsOnboardingCompleted == false)
      latestNavController.navigateClearStack<NavRoute.Splash>(NavRoute.LanguageSelection)
    else latestNavController.navigateClearStack<NavRoute.Splash>(NavRoute.Main)
  }

  BackHandler { }

  SplashContent(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(32.dp)
      .navigationBarsPadding()
  )
}

@Preview
@Composable
private fun SplashScreenPreview() {
  DexReaderTheme {
    SplashScreen(
      navController = rememberNavController(),
      isOnboardingCompleted = false,
      modifier = Modifier.fillMaxSize(),
    )
  }
}
