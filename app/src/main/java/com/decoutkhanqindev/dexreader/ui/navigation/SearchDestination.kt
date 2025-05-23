package com.decoutkhanqindev.dexreader.ui.navigation

object SearchDestination : NavigationDestination {
  override val route: String = "search"
  val routeWithArgs: String = "search/{mangaId}"
}