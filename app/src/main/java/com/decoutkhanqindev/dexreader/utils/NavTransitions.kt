package com.decoutkhanqindev.dexreader.utils

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
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

/**
 * Navigation and screen transitions utility
 * Contains navigation helpers and reusable transition animations
 */
object NavTransitions {

  // Transition Animation Constants
  private const val ANIMATION_DURATION = 500
  private const val OFFSET_X = 500

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
  fun slideFromLeftTransitions(): NavTransitions = NavTransitions(
    enter = {
      slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { -OFFSET_X }) +
          fadeIn(animationSpec = fadeAnimationSpec)
    },
    exit = {
      slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { -OFFSET_X }) +
          fadeOut(animationSpec = fadeAnimationSpec)
    },
    popEnter = {
      slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { -OFFSET_X }) +
          fadeIn(animationSpec = fadeAnimationSpec)
    },
    popExit = {
      slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { -OFFSET_X }) +
          fadeOut(animationSpec = fadeAnimationSpec)
    }
  )

  /**
   * Slide from RIGHT transition (for most screens)
   * Enter: slides in from right with fade
   * Exit: slides out to left with fade
   * PopEnter: slides in from left with fade
   * PopExit: slides out to right with fade
   */
  fun slideFromRightTransitions(): NavTransitions = NavTransitions(
    enter = {
      slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { OFFSET_X }) +
          fadeIn(animationSpec = fadeAnimationSpec)
    },
    exit = {
      slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { -OFFSET_X }) +
          fadeOut(animationSpec = fadeAnimationSpec)
    },
    popEnter = {
      slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { -OFFSET_X }) +
          fadeIn(animationSpec = fadeAnimationSpec)
    },
    popExit = {
      slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { OFFSET_X }) +
          fadeOut(animationSpec = fadeAnimationSpec)
    }
  )

  /**
   * Simple ENTER ONLY transition (for Reader, Register, ForgotPassword screens)
   * Only enter and popExit animations
   */
  fun slideEnterOnlyTransitions(): NavTransitions = NavTransitions(
    enter = {
      slideInHorizontally(animationSpec = slideAnimationSpec, initialOffsetX = { OFFSET_X }) +
          fadeIn(animationSpec = fadeAnimationSpec)
    },
    exit = null,
    popEnter = null,
    popExit = {
      slideOutHorizontally(animationSpec = slideAnimationSpec, targetOffsetX = { OFFSET_X }) +
          fadeOut(animationSpec = fadeAnimationSpec)
    }
  )

  // Data class to hold navigation transition animations
  data class NavTransitions(
    val enter: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
    val exit: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
    val popEnter: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?,
    val popExit: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?,
  )

  // Extension functions for NavHostController
  fun NavHostController.navigatePreserveState(route: String) {
    this.navigate(route) {
      popUpTo(graph.startDestinationId) {
        saveState = true     // Saves state
      }
      launchSingleTop = true // Prevents duplicates
      restoreState = true    // Restores state when back
    }
  }

  fun NavHostController.navigateClearStack(
    currentRoute: String,
    destination: String,
  ) {
    this.navigate(destination) {
      popUpTo(currentRoute) {
        inclusive = true     // Removes destination from stack
      }
      launchSingleTop = true // Prevents duplicates
    }
  }
}
