package com.decoutkhanqindev.dexreader.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.screens.splash.components.SplashContent
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  modifier: Modifier = Modifier,
  onNavigateToHome: () -> Unit,
) {
  val splashDurationMs = 3000L
  LaunchedEffect(Unit) {
    delay(splashDurationMs)
    onNavigateToHome()
  }

  SplashContent(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(32.dp)
  )
}

@Preview
@Composable
private fun SplashScreenPreview() {
  DexReaderTheme {
    SplashScreen(
      modifier = Modifier.fillMaxSize(),
      onNavigateToHome = {},
    )
  }
}
