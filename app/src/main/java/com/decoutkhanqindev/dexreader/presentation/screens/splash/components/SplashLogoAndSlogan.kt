package com.decoutkhanqindev.dexreader.presentation.screens.splash.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashLogoAndSlogan(modifier: Modifier = Modifier) {
  val enterSpec = tween<Float>(durationMillis = 900, easing = FastOutSlowInEasing)
  val exitSpec = tween<Float>(durationMillis = 700, easing = FastOutSlowInEasing)
  val visiblePauseMs = 800L
  val cycleGapMs = 300L
  val offsetY = -80f
  val appNameAlpha = remember { Animatable(0f) }
  val appNameOffsetY = remember { Animatable(offsetY) }
  val sloganAlpha = remember { Animatable(0f) }
  val sloganOffsetY = remember { Animatable(offsetY) }

  LaunchedEffect(Unit) {
    while (true) {
      launch { appNameOffsetY.animateTo(0f, enterSpec) }
      appNameAlpha.animateTo(1f, enterSpec)

      launch { sloganOffsetY.animateTo(0f, enterSpec) }
      sloganAlpha.animateTo(1f, enterSpec)

      delay(visiblePauseMs)

      launch { appNameOffsetY.animateTo(offsetY, exitSpec) }
      appNameAlpha.animateTo(0f, exitSpec)

      launch { sloganOffsetY.animateTo(offsetY, exitSpec) }
      sloganAlpha.animateTo(0f, exitSpec)

      delay(cycleGapMs)
    }
  }

  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    Image(
      painter = painterResource(R.drawable.app_icon),
      contentDescription = null,
      modifier = Modifier
        .size(96.dp)
        .shimmer(true),
    )

    Text(
      text = stringResource(R.string.app_name),
      style = MaterialTheme.typography.headlineLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onBackground,
      modifier = Modifier.graphicsLayer(
        alpha = appNameAlpha.value,
        translationY = appNameOffsetY.value,
      ),
    )

    Text(
      text = stringResource(R.string.slogan),
      style = MaterialTheme.typography.bodyLarge,
      fontStyle = FontStyle.Italic,
      color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
      textAlign = TextAlign.Center,
      modifier = Modifier
        .padding(horizontal = 32.dp)
        .graphicsLayer(
          alpha = sloganAlpha.value,
          translationY = sloganOffsetY.value,
        ),
    )
  }
}
