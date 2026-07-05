package com.decoutkhanqindev.dexreader.util

import android.os.SystemClock
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

object NavTransitions {

  private const val ANIMATION_DURATION = 700
  private const val FADE_ANIMATION_DURATION = 400
  private const val OFFSET_X = 700
  private const val NAVIGATION_DEBOUNCE_TIME = 500L

  private val slideAnimationSpec: FiniteAnimationSpec<IntOffset> = tween(
    durationMillis = ANIMATION_DURATION,
    easing = FastOutSlowInEasing
  )

  private val fadeAnimationSpec: FiniteAnimationSpec<Float> = tween(ANIMATION_DURATION)
  private val shortFadeAnimationSpec: FiniteAnimationSpec<Float> = tween(FADE_ANIMATION_DURATION)

  fun fadeTransitions() = NavTransitions(
    enter = { fadeIn(animationSpec = shortFadeAnimationSpec) },
    exit = { fadeOut(animationSpec = shortFadeAnimationSpec) },
    popEnter = { fadeIn(animationSpec = shortFadeAnimationSpec) },
    popExit = { fadeOut(animationSpec = shortFadeAnimationSpec) },
  )

  fun slideFromLeftTransitions() = NavTransitions(
    enter = {
      slideInHorizontally(
        animationSpec = slideAnimationSpec,
        initialOffsetX = { -OFFSET_X }
      ) + fadeIn(animationSpec = fadeAnimationSpec)
    },
    exit = {
      slideOutHorizontally(
        animationSpec = slideAnimationSpec,
        targetOffsetX = { -OFFSET_X }
      ) + fadeOut(animationSpec = fadeAnimationSpec)
    },
    popEnter = {
      slideInHorizontally(
        animationSpec = slideAnimationSpec,
        initialOffsetX = { -OFFSET_X }
      ) + fadeIn(animationSpec = fadeAnimationSpec)
    },
    popExit = {
      slideOutHorizontally(
        animationSpec = slideAnimationSpec,
        targetOffsetX = { -OFFSET_X }
      ) + fadeOut(animationSpec = fadeAnimationSpec)
    }
  )

  fun slideFromRightTransitions() = NavTransitions(
    enter = {
      slideInHorizontally(
        animationSpec = slideAnimationSpec,
        initialOffsetX = { OFFSET_X }
      ) + fadeIn(animationSpec = fadeAnimationSpec)
    },
    exit = {
      slideOutHorizontally(
        animationSpec = slideAnimationSpec,
        targetOffsetX = { -OFFSET_X }
      ) + fadeOut(animationSpec = fadeAnimationSpec)
    },
    popEnter = {
      slideInHorizontally(
        animationSpec = slideAnimationSpec,
        initialOffsetX = { -OFFSET_X }
      ) + fadeIn(animationSpec = fadeAnimationSpec)
    },
    popExit = {
      slideOutHorizontally(
        animationSpec = slideAnimationSpec,
        targetOffsetX = { OFFSET_X }
      ) + fadeOut(animationSpec = fadeAnimationSpec)
    }
  )

  fun slideEnterOnlyTransitions() = NavTransitions(
    enter = {
      slideInHorizontally(
        animationSpec = slideAnimationSpec,
        initialOffsetX = { OFFSET_X }
      ) + fadeIn(animationSpec = fadeAnimationSpec)
    },
    exit = null,
    popEnter = null,
    popExit = {
      slideOutHorizontally(
        animationSpec = slideAnimationSpec,
        targetOffsetX = { OFFSET_X }
      ) + fadeOut(animationSpec = fadeAnimationSpec)
    }
  )

  fun slideExitOnlyTransitions() = NavTransitions(
    enter = null,
    exit = {
      slideOutHorizontally(
        animationSpec = slideAnimationSpec,
        targetOffsetX = { -OFFSET_X }
      ) + fadeOut(animationSpec = fadeAnimationSpec)
    },
    popEnter = {
      slideInHorizontally(
        animationSpec = slideAnimationSpec,
        initialOffsetX = { -OFFSET_X }
      ) + fadeIn(animationSpec = fadeAnimationSpec)
    },
    popExit = null
  )

  @Immutable
  data class NavTransitions(
    val enter: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
    val exit: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
    val popEnter: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
    val popExit: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
  )

  fun NavHostController.navigatePreserveState(route: Any) {
    this.navigateTo(route) {
      popUpTo(graph.startDestinationId) {
        saveState = true
      }
      launchSingleTop = true
      restoreState = true
    }
  }

  inline fun <reified T : Any> NavHostController.navigateClearStack(route: Any) {
    this.navigateTo(route) {
      popUpTo<T> {
        inclusive = true
      }
      launchSingleTop = true
    }
  }

  fun NavHostController.navigateTo(
    route: Any,
    builder: NavOptionsBuilder.() -> Unit = {},
  ) {
    tryNavigate { this.navigate(route, builder) }
  }

  fun NavHostController.navigateBack() {
    tryNavigate { this.popBackStack() }
  }

  private var lastClickTime = 0L

  private fun tryNavigate(
    debounceTime: Long = NAVIGATION_DEBOUNCE_TIME,
    action: () -> Unit,
  ) {
    val currentTime = SystemClock.uptimeMillis()
    if (currentTime - lastClickTime >= debounceTime) {
      action()
      lastClickTime = currentTime
    }
  }
}
