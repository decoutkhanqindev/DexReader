package com.decoutkhanqindev.dexreader.utils

import androidx.navigation.NavHostController

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