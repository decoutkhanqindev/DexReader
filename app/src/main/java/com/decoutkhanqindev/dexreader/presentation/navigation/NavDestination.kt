package com.decoutkhanqindev.dexreader.presentation.navigation


sealed interface NavDestination {
  val route: String

  object LoginDestination : NavDestination {
    override val route: String = "login"
  }

  object RegisterDestination : NavDestination {
    override val route: String = "register"
  }

  object ForgotPasswordDestination : NavDestination {
    override val route: String = "forgot_password"
  }

  object HomeDestination : NavDestination {
    override val route: String = "home"
  }

  object CategoriesDestination : NavDestination {
    override val route: String = "categories"
  }

  object CategoryDetailsDestination : NavDestination {
    override val route: String = "category_details"
    const val CATEGORY_ID_ARG: String = "categoryId"
    const val CATEGORY_TITLE_ARG: String = "categoryTitle"
    val routeWithArgs: String = "${route}/{$CATEGORY_ID_ARG}/{$CATEGORY_TITLE_ARG}"
  }

  object FavoritesDestination : NavDestination {
    override val route: String = "favorites"
  }

  object HistoryDestination : NavDestination {
    override val route: String = "history"
  }

  object ProfileDestination : NavDestination {
    override val route: String = "profile"
  }

  object SettingsDestination : NavDestination {
    override val route: String = "settings"
  }

  object SearchDestination : NavDestination {
    override val route: String = "search"
  }

  object MangaDetailsDestination : NavDestination {
    override val route: String = "manga_details"
    const val MANGA_ID_ARG: String = "mangaId"
    val routeWithArgs: String = "$route/{$MANGA_ID_ARG}"
  }

  object ReaderDestination : NavDestination {
    override val route: String = "reader"
    const val CHAPTER_ID_ARG: String = "chapterId"
    val routeWithArgs: String = "$route/{$CHAPTER_ID_ARG}"
  }
}

