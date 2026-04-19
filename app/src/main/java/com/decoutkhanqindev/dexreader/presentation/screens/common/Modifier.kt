package com.decoutkhanqindev.dexreader.presentation.screens.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import kotlinx.collections.immutable.persistentListOf

@Composable
fun Modifier.onScalableClick(
  shape: Shape? = null,
  block: () -> Unit,
): Modifier {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()
  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.95f else 1f,
    animationSpec = tween(200),
    label = "on_scalable_click_animation"
  )

  return this
    .graphicsLayer {
      scaleX = scale
      scaleY = scale
    }
    .let {
      if (shape != null) it.clip(shape) else it
    }
    .clickable(
      interactionSource = interactionSource,
      indication = ripple(),
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

@Composable
fun Modifier.normalShimmer(
  isEnable: Boolean = true,
  bounds: ShimmerBounds = ShimmerBounds.View,
): Modifier {
  val shimmer = rememberShimmer(shimmerBounds = bounds)
  return this.let {
    if (isEnable) it.shimmer(customShimmer = shimmer) else it
  }
}

@Composable
fun Modifier.blurBackground(
  color: Color = MaterialTheme.colorScheme.surface,
  topAlpha: Float = 0.7f,
  bottomAlpha: Float = 1f,
): Modifier = this.background(
  brush = Brush.verticalGradient(
    colors = persistentListOf(
      color.copy(topAlpha),
      color.copy(bottomAlpha)
    )
  )
)