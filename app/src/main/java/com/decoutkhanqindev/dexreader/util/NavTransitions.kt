package com.decoutkhanqindev.dexreader.util

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute

object NavTransitions {
  inline fun <reified Root : NavRoute> NavHostController.navigatePreserveState(route: NavRoute) {
    this.navigateTo(route) {
      popUpTo<Root> { saveState = true }
      launchSingleTop = true
      restoreState = true
    }
  }

  inline fun <reified T : NavRoute> NavHostController.navigateClearStack(route: NavRoute) {
    this.navigateTo(route) {
      popUpTo<T> { inclusive = true }
      launchSingleTop = true
    }
  }

  fun NavHostController.navigateTo(
    route: NavRoute,
    builder: NavOptionsBuilder.() -> Unit = {},
  ) {
    this.navigate(route, builder)
  }

  fun NavHostController.navigateBack() {
    this.popBackStack()
  }
}
