package com.decoutkhanqindev.dexreader.util

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

object NavTransitions {
  inline fun <reified Root : Any> NavHostController.navigatePreserveState(route: Any) {
    this.navigateTo(route) {
      popUpTo<Root> { saveState = true }
      launchSingleTop = true
      restoreState = true
    }
  }

  inline fun <reified T : Any> NavHostController.navigateClearStack(route: Any) {
    this.navigateTo(route) {
      popUpTo<T> { inclusive = true }
      launchSingleTop = true
    }
  }

  fun NavHostController.navigateTo(
    route: Any,
    builder: NavOptionsBuilder.() -> Unit = {},
  ) {
    this.navigate(route, builder)
  }

  fun NavHostController.navigateBack() {
    this.popBackStack()
  }
}
