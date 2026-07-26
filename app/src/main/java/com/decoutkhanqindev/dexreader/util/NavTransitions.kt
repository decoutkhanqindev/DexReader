package com.decoutkhanqindev.dexreader.util

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

object NavTransitions {

  private const val NAVIGATION_DEBOUNCE_TIME = 300L
  private var lastNavigateTime = 0L

  inline fun <reified Root : Any> NavHostController.navigatePreserveState(route: Any) {
    this.navigateTo(route) {
      popUpTo<Root> {
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

  private fun tryNavigate(action: () -> Unit) {
    val currentTime = System.currentTimeMillis()
    if (currentTime - lastNavigateTime >= NAVIGATION_DEBOUNCE_TIME) {
      action()
      lastNavigateTime = currentTime
    }
  }
}
