package com.decoutkhanqindev.dexreader.presentation.ui.manga_details

import com.decoutkhanqindev.dexreader.presentation.navigation.NavigationDestination

object MangaDetailsDestination: NavigationDestination {
  override val route: String = "manga_details"
  val mangaIdArg: String = "mangaId"
  val routeWithArgs: String = "$route/{$mangaIdArg}"
}