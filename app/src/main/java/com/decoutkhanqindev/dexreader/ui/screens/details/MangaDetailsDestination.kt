package com.decoutkhanqindev.dexreader.ui.screens.details

import com.decoutkhanqindev.dexreader.ui.navigation.NavigationDestination

object MangaDetailsDestination: NavigationDestination {
  override val route: String = "manga_details"
  val mangaIdArg: String = "mangaId"
  val routeWithArgs: String = "$route/{$mangaIdArg}"
}