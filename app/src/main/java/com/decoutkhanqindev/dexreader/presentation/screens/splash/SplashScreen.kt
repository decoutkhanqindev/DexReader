package com.decoutkhanqindev.dexreader.presentation.screens.splash

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
import com.decoutkhanqindev.dexreader.presentation.screens.splash.components.SplashContent
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  isOnboardingCompleted: Boolean?,
  modifier: Modifier = Modifier,
  onNavigateToLanguageSelectionScreen: () -> Unit,
  onNavigateToMainScreen: () -> Unit,
) {
  val latestIsOnboardingCompleted by rememberUpdatedState(isOnboardingCompleted)
  val latestOnNavigateToLanguageSelectionScreen by rememberUpdatedState(onNavigateToLanguageSelectionScreen)
  val latestOnNavigateToMainScreen by rememberUpdatedState(onNavigateToMainScreen)

  LaunchedEffect(Unit) {
    delay(3000L)
    if (latestIsOnboardingCompleted == false) latestOnNavigateToLanguageSelectionScreen()
    else latestOnNavigateToMainScreen()
  }

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
      isOnboardingCompleted = false,
      modifier = Modifier.fillMaxSize(),
      onNavigateToLanguageSelectionScreen = {},
      onNavigateToMainScreen = {},
    )
  }
}
