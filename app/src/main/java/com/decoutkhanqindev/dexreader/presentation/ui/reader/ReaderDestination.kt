package com.decoutkhanqindev.dexreader.presentation.ui.reader

import com.decoutkhanqindev.dexreader.presentation.navigation.NavigationDestination

object ReaderDestination: NavigationDestination {
  override val route: String = "reader"
  val chapterIdArg: String = "chapterId"
  val routeWithArgs: String = "$route/{$chapterIdArg}"
}