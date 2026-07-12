package com.decoutkhanqindev.dexreader.presentation.screens.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.spring
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
  val scale = animateFloatAsState(
    targetValue = if (isPressed) 0.9f else 1f,
    animationSpec = tween(200),
    label = "on_scalable_click_animation"
  )

  return this
    .graphicsLayer {
      scaleX = scale.value
      scaleY = scale.value
    }
    .then(if (shape != null) Modifier.clip(shape) else Modifier)
    .clickable(
      interactionSource = interactionSource,
      indication = ripple(),
      onClick = block,
    )
}

@Composable
fun Modifier.shimmer(
  isEnable: Boolean = true,
  durationMillis: Int = 1000,
  bounds: ShimmerBounds = ShimmerBounds.View,
): Modifier {
  val theme = defaultShimmerTheme.copy(
    animationSpec = infiniteRepeatable(
      animation = tween(
        durationMillis = durationMillis,
        easing = LinearEasing
      ),
      repeatMode = RepeatMode.Restart
    )
  )
  val shimmer = rememberShimmer(
    shimmerBounds = bounds,
    theme = theme
  )

  return this.then(if (isEnable) Modifier.shimmer(customShimmer = shimmer) else Modifier)
}

@Composable
fun Modifier.blurBackground(
  color: Color = MaterialTheme.colorScheme.surfaceContainer,
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

@Composable
fun Modifier.animateItemOnAppear(): Modifier {
  val visibleState = remember {
    MutableTransitionState(false).apply { targetState = true }
  }

  val transition = rememberTransition(visibleState, label = "ItemAppearance")

  val alpha = transition.animateFloat(
    transitionSpec = { spring(stiffness = Spring.StiffnessLow) },
    label = "Alpha"
  ) { if (it) 1f else 0f }

  val scale = transition.animateFloat(
    transitionSpec = {
      spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
      )
    },
    label = "Scale"
  ) { if (it) 1f else 0.8f }

  val translationY = transition.animateFloat(
    transitionSpec = { spring(stiffness = Spring.StiffnessLow) },
    label = "TranslationY"
  ) { if (it) 0f else 50f }

  return this.graphicsLayer {
    this.alpha = alpha.value
    this.scaleX = scale.value
    this.scaleY = scale.value
    this.translationY = translationY.value
  }
}