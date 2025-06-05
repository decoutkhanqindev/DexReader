package com.decoutkhanqindev.dexreader.presentation.navigation


sealed interface NavigationDestination {
  val route: String

  object SignInScreen : NavigationDestination {
    override val route: String = "sign_in"
  }

  object SignUpScreen : NavigationDestination {
    override val route: String = "sign_up"
  }

  object HomeScreen : NavigationDestination {
    override val route: String = "home"
  }

  object TagsScreen : NavigationDestination {
    override val route: String = "tag"
  }

  object CategoryDetailsScreen : NavigationDestination {
    override val route: String = "category_details"
  }

  object FavoriteScreen : NavigationDestination {
    override val route: String = "favorite"
  }

  object HistoryScreen : NavigationDestination {
    override val route: String = "history"
  }

  object ProfileScreen : NavigationDestination {
    override val route: String = "profile"
  }

  object SettingsScreen : NavigationDestination {
    override val route: String = "settings"
  }

  object SearchScreen : NavigationDestination {
    override val route: String = "search"
  }

  object MangaDetailsScreen : NavigationDestination {
    override val route: String = "manga_details"
    const val MANGA_ID_ARG: String = "mangaId"
    val routeWithArgs: String = "$route/{$MANGA_ID_ARG}"
  }

  object ReaderScreen : NavigationDestination {
    override val route: String = "reader"
    const val CHAPTER_ID_ARG: String = "chapterId"
    val routeWithArgs: String = "$route/{$CHAPTER_ID_ARG}"
  }
}

