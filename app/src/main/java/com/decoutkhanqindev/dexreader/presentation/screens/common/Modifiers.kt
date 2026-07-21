package com.decoutkhanqindev.dexreader.presentation.screens.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun Modifier.onClick(
  shape: Shape? = null,
  ripple: Boolean = true,
  action: () -> Unit,
): Modifier = composed {
  var isPressed by remember { mutableStateOf(false) }
  val interactionSource = remember { MutableInteractionSource() }
  val scale = animateFloatAsState(
    targetValue = if (isPressed) 0.95f else 1f,
    animationSpec = tween(durationMillis = 100),
    label = "OnScalableClickScaleAnimation"
  )

  this
    .graphicsLayer {
      scaleX = scale.value
      scaleY = scale.value
    }
    .then(if (shape != null) this.clip(shape) else this)
    .pointerInput(Unit) {
      awaitEachGesture {
        awaitFirstDown(requireUnconsumed = false)
        isPressed = true
        waitForUpOrCancellation()
        isPressed = false
      }
    }
    .then(
      if (ripple) {
        this.clickable(
          interactionSource = interactionSource,
          indication = ripple(),
          onClick = action,
        )
      } else {
        this.clickable(
          interactionSource = null,
          indication = null,
          onClick = action,
        )
      }
    )
}

private val ShimmerCosA by lazy { cos((20.0 * PI / 180.0).toFloat()) }
private val ShimmerSinA by lazy { sin((20.0 * PI / 180.0).toFloat()) }

private fun DrawScope.drawShimmerGradient(progress: Float, highlightColor: Color) {
  val bandHalf = 100f
  val cx = size.width / 2f
  val cy = size.height / 2f
  val bx = progress * (size.width + bandHalf * 4) - bandHalf * 2
  val startRelX = bx - bandHalf - cx
  val endRelX = bx + bandHalf - cx

  drawRect(
    brush = Brush.linearGradient(
      colors = listOf(Color.Transparent, highlightColor, Color.Transparent),
      start = Offset(startRelX * ShimmerCosA + cx, startRelX * ShimmerSinA + cy),
      end = Offset(endRelX * ShimmerCosA + cx, endRelX * ShimmerSinA + cy),
    ),
  )
}

@Composable
fun Modifier.shimmerLoading(
  backgroundColor: Color? = null,
  shimmerColor: Color = Color.White.copy(alpha = 0.3f),
  shape: Shape? = null,
  isEnable: Boolean = true,
  durationMillis: Int = 1000,
): Modifier {
  if (!isEnable) return this

  val transition = rememberInfiniteTransition(label = "ShimmerLoading")
  val progress = transition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(
        durationMillis = durationMillis,
        easing = LinearEasing
      ),
      repeatMode = RepeatMode.Restart,
    ),
    label = "ShimmerLoadingProgress",
  )

  return this
    .then(if (shape != null) Modifier.clip(shape) else Modifier)
    .drawWithContent {
      if (backgroundColor != null) drawRect(backgroundColor)
      drawContent()
      drawShimmerGradient(progress.value, shimmerColor)
    }
}

@Composable
fun Modifier.shimmerHighlight(
  backgroundColor: Color,
  shimmerColor: Color,
  shape: Shape? = null,
  durationMillis: Int = 1400,
): Modifier {
  val transition = rememberInfiniteTransition(label = "ShimmerHighlight")
  val progress = transition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = durationMillis, easing = LinearEasing),
      repeatMode = RepeatMode.Restart,
    ),
    label = "ShimmerHighlightProgress",
  )
  return this
    .then(if (shape != null) Modifier.clip(shape) else Modifier)
    .drawWithContent {
      drawRect(backgroundColor)
      drawShimmerGradient(progress.value, shimmerColor)
      drawContent()
    }
}

@Composable
fun Modifier.blurBackground(
  color: Color = MaterialTheme.colorScheme.surfaceContainer,
  topAlpha: Float = 0.7f,
  topCenterAlpha: Float? = null,
  bottomCenterAlpha: Float? = null,
  bottomAlpha: Float = 1f,
  startY: Float = 0f,
  endY: Float = Float.POSITIVE_INFINITY,
): Modifier = this.background(
  brush = Brush.verticalGradient(
    colors = persistentListOf(
      color.copy(topAlpha),
      color.copy(topCenterAlpha ?: topAlpha),
      color.copy(bottomCenterAlpha ?: bottomAlpha),
      color.copy(bottomAlpha)
    ),
    startY = startY,
    endY = endY,
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
