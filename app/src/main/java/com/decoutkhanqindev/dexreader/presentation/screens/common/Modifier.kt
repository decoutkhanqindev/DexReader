package com.decoutkhanqindev.dexreader.presentation.screens.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun Modifier.onScalableClick(
  rippleRadius: Dp,
  block: () -> Unit,
): Modifier {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()
  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.95f else 1f,
    animationSpec = tween(100),
  )

  return this
    .graphicsLayer {
      scaleX = scale
      scaleY = scale
    }
    .clickable(
      interactionSource = interactionSource,
      indication = ripple(radius = rippleRadius),
      onClick = block,
    )
}

@Composable
fun Modifier.fastShimmer(
  isEnable: Boolean = true,
  bounds: ShimmerBounds = ShimmerBounds.View,
): Modifier {
  val theme = defaultShimmerTheme.copy(
    animationSpec = infiniteRepeatable(
      animation = tween(
        durationMillis = 800,
        easing = LinearEasing
      ),
      repeatMode = RepeatMode.Restart
    )
  )
  val shimmer = rememberShimmer(
    shimmerBounds = bounds,
    theme = theme
  )

  return this.let {
    if (isEnable) it.shimmer(customShimmer = shimmer) else it
  }
}
