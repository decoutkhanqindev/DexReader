package com.decoutkhanqindev.dexreader.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface NavRoute {

  @Serializable
  data object Login : NavRoute

  @Serializable
  data object Register : NavRoute

  @Serializable
  data object ForgotPassword : NavRoute

  @Serializable
  data object Home : NavRoute

  @Serializable
  data object Categories : NavRoute

  @Serializable
  data object Favorites : NavRoute

  @Serializable
  data object History : NavRoute

  @Serializable
  data object Profile : NavRoute

  @Serializable
  data object Settings : NavRoute

  @Serializable
  data object Search : NavRoute

  @Serializable
  data class MangaDetails(val mangaId: String) : NavRoute

  @Serializable
  data class CategoryDetails(
    val categoryId: String,
    val categoryTitle: String,
  ) : NavRoute

  @Serializable
  data class Reader(
    val chapterId: String,
    val lastReadPage: Int = 0,
    val mangaId: String,
  ) : NavRoute
}
