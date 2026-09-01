package com.decoutkhanqindev.dexreader.presentation.screens.onboarding.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun OnboardingPageIndicator(
  pageCount: Int,
  selectedPage: Int,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    verticalAlignment = Alignment.CenterVertically
  ) {
    repeat(pageCount) { index ->
      val isSelected = index == selectedPage
      val width by animateDpAsState(
        targetValue = if (isSelected) 28.dp else 8.dp,
        animationSpec = tween(durationMillis = 250),
        label = "OnboardingPageIndicatorWidthAnimation"
      )
      val color by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f),
        animationSpec = tween(durationMillis = 250),
        label = "OnboardingPageIndicatorColorAnimation"
      )

      Box(
        modifier = Modifier
          .width(width)
          .height(8.dp)
          .background(color = color, shape = CircleShape)
      )
    }
  }
}

@Preview
@Composable
private fun OnboardingPageIndicatorPreview() {
  DexReaderTheme {
    OnboardingPageIndicator(
      pageCount = 4,
      selectedPage = 1,
      modifier = Modifier.fillMaxWidth(),
    )
  }
}
