package com.decoutkhanqindev.dexreader.ui.navigation

object HomeDestination : NavigationDestination {
  override val route: String = "home"
  val routeWithArgs: String = "home/{mangaId}"
}