package com.decoutkhanqindev.dexreader.presentation.navigation


sealed interface NavigationDestination {
  val route: String

  object LoginDestination : NavigationDestination {
    override val route: String = "login"
  }

  object RegisterDestination : NavigationDestination {
    override val route: String = "register"
  }

  object HomeDestination : NavigationDestination {
    override val route: String = "home"
  }

  object CategoriesDestination : NavigationDestination {
    override val route: String = "categories"
  }

  object CategoryDetailsDestination : NavigationDestination {
    override val route: String = "category_details"
    const val CATEGORY_ID_ARG: String = "categoryId"
    const val CATEGORY_TITLE_ARG: String = "categoryTitle"
    val routeWithArgs: String = "${route}/{$CATEGORY_ID_ARG}/{$CATEGORY_TITLE_ARG}"
  }

  object FavoriteDestination : NavigationDestination {
    override val route: String = "favorite"
  }

  object HistoryDestination : NavigationDestination {
    override val route: String = "history"
  }

  object ProfileDestination : NavigationDestination {
    override val route: String = "profile"
  }

  object SettingsDestination : NavigationDestination {
    override val route: String = "settings"
  }

  object SearchDestination : NavigationDestination {
    override val route: String = "search"
  }

  object MangaDetailsDestination : NavigationDestination {
    override val route: String = "manga_details"
    const val MANGA_ID_ARG: String = "mangaId"
    val routeWithArgs: String = "$route/{$MANGA_ID_ARG}"
  }

  object ReaderDestination : NavigationDestination {
    override val route: String = "reader"
    const val CHAPTER_ID_ARG: String = "chapterId"
    val routeWithArgs: String = "$route/{$CHAPTER_ID_ARG}"
  }
}

