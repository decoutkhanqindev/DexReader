package com.decoutkhanqindev.dexreader.utils

import androidx.navigation.NavHostController

fun NavHostController.navigateTo(route: String) {
  this.navigate(route) {
    // Clears back stack up to start destination
    popUpTo(graph.startDestinationId) {
      saveState = true
    }

    // Prevents multiple copies of same destination in back stack
    launchSingleTop = true

    // Restores previous state when navigating back
    restoreState = true
  }
}