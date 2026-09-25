package com.decoutkhanqindev.dexreader.presentation.screens.splash.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.animation.AnimatedLogoAndSlogan
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashContent(
  isNetworkAvailable: () -> Boolean,
  modifier: Modifier = Modifier,
) {
  val progress = remember { Animatable(0f) }

  LaunchedEffect(Unit) {
    snapshotFlow { isNetworkAvailable() }.collectLatest {
      if (it) progress.animateTo(
        targetValue = 0.99f,
        animationSpec = tween(
          durationMillis = (5000 * (0.99f - progress.value) / 0.99f).toInt()
        ),
      )
    }
  }

  Box(
    modifier = modifier.background(
      Brush.radialGradient(
        colors = listOf(
          MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
          Color.Transparent,
        )
      )
    )
  ) {
    AnimatedLogoAndSlogan(
      modifier = Modifier.align(Alignment.Center),
      logoSize = 120.dp,
    )

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.BottomCenter),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      LoadingProgress(
        progress = { progress.value },
        modifier = Modifier.fillMaxWidth(),
      )

      Text(
        text = stringResource(R.string.may_contain_ads),
        style = MaterialTheme.typography.bodySmall,
        fontStyle = FontStyle.Italic,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}
