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

/**
 * Navigation and screen transitions utility
 * Contains navigation helpers and reusable transition animations
 */
object NavTransitions {

  // Transition Animation Constants
  private const val ANIMATION_DURATION = 700
  private const val OFFSET_X = 700

  // Navigation debounce time to prevent rapid multiple navigations
  private const val NAVIGATION_DEBOUNCE_TIME = 500L

  private val slideAnimationSpec: FiniteAnimationSpec<IntOffset> = tween(
    durationMillis = ANIMATION_DURATION,
    easing = FastOutSlowInEasing
  )

  private val fadeAnimationSpec: FiniteAnimationSpec<Float> = tween(ANIMATION_DURATION)

  /**
   * Slide from LEFT transition (for Home screen)
   * Enter: slides in from left with fade
   * Exit: slides out to left with fade
   */
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

  /**
   * Slide from RIGHT transition (for most screens)
   * Enter: slides in from right with fade
   * Exit: slides out to left with fade
   * PopEnter: slides in from left with fade
   * PopExit: slides out to right with fade
   */
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

  /**
   * Simple ENTER ONLY transition (for Reader, Register, ForgotPassword screens)
   * Only enter and popExit animations
   */
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

  // Data class to hold navigation transition animations
  @Immutable
  data class NavTransitions(
    val enter: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
    val exit: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
    val popEnter: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
    val popExit: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
  )

  // Extension functions for NavHostController
  fun NavHostController.navigatePreserveState(route: Any) {
    this.navigateToWithDebounce(route) {
      popUpTo(graph.startDestinationId) {
        saveState = true     // Saves state
      }
      launchSingleTop = true // Prevents duplicates
      restoreState = true    // Restores state when back
    }
  }

  inline fun <reified T : Any> NavHostController.navigateClearStack(route: Any) {
    this.navigateToWithDebounce(route) {
      popUpTo<T> {
        inclusive = true     // Removes current screen from stack
      }
      launchSingleTop = true // Prevents duplicates
    }
  }

  fun NavHostController.navigateToWithDebounce(
    route: Any,
    debounceTime: Long = NAVIGATION_DEBOUNCE_TIME,
    builder: NavOptionsBuilder.() -> Unit = {},
    ) {
    var lastClickTime = 0L
    // SystemClock.uptimeMillis() is used to get the time in milliseconds since the system was booted,
    // including time spent in sleep. This is ideal for measuring time intervals, such as debouncing clicks,
    // because it is not affected by changes in the system clock (e.g., due to user adjustments or daylight saving time).
    val currentTime = SystemClock.uptimeMillis()
    if (currentTime - lastClickTime >= debounceTime) {
      this.navigate(route, builder)
      lastClickTime = currentTime
    }
  }

  fun NavHostController.navigateBackWithDebounce(debounceTime: Long = NAVIGATION_DEBOUNCE_TIME) {
    var lastClickTime = 0L
    val currentTime = SystemClock.uptimeMillis()
    if (currentTime - lastClickTime >= debounceTime) {
      this.popBackStack()
      lastClickTime = currentTime
    }
  }
}
