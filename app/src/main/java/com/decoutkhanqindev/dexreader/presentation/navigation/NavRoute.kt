package com.decoutkhanqindev.dexreader.presentation.navigation

import kotlinx.serialization.Serializable

object NavRoute {

  @Serializable
  data object Login

  @Serializable
  data object Register

  @Serializable
  data object ForgotPassword

  @Serializable
  data object Home

  @Serializable
  data object Categories

  @Serializable
  data object Favorites

  @Serializable
  data object History

  @Serializable
  data object Profile

  @Serializable
  data object Settings

  @Serializable
  data object Search

  @Serializable
  data class MangaDetails(val mangaId: String)

  @Serializable
  data class CategoryDetails(
    val categoryId: String,
    val categoryTitle: String,
  )

  @Serializable
  data class Reader(
    val chapterId: String,
    val lastReadPage: Int = 0,
    val mangaId: String,
  )
}
