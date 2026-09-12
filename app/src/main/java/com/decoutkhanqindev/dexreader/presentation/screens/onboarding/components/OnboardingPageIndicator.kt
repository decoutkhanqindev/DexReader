package com.decoutkhanqindev.dexreader.presentation.screens.onboarding.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlin.math.absoluteValue

@Composable
fun OnboardingPageIndicator(
  pageCount: Int,
  selectedPage: Int,
  modifier: Modifier = Modifier,
) {
  val dotSize = 8.dp
  val selectedDotWidth = 28.dp
  val dotSpacing = 8.dp
  val selectedColor = MaterialTheme.colorScheme.primary
  val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)

  val animatedPage = animateFloatAsState(
    targetValue = selectedPage.toFloat(),
    animationSpec = tween(durationMillis = 250),
    label = "OnboardingPageIndicatorAnimation"
  )

  Canvas(modifier = modifier.height(dotSize)) {
    val page = animatedPage.value
    val dotPx = dotSize.toPx()
    val selectedDotPx = selectedDotWidth.toPx()
    val spacingPx = dotSpacing.toPx()
    val cornerRadius = CornerRadius(dotPx / 2f)
    val totalWidth = dotPx * (pageCount - 1) +
        selectedDotPx +
        spacingPx * (pageCount - 1)

    var offsetX = (size.width - totalWidth) / 2f

    repeat(pageCount) { index ->
      val fraction = (1f - (index - page).absoluteValue).coerceIn(0f, 1f)
      val width = dotPx + (selectedDotPx - dotPx) * fraction

      drawRoundRect(
        color = lerp(unselectedColor, selectedColor, fraction),
        topLeft = Offset(offsetX, 0f),
        size = Size(width, dotPx),
        cornerRadius = cornerRadius,
      )

      offsetX += width + spacingPx
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
