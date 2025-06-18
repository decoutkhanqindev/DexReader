package com.decoutkhanqindev.dexreader.presentation.screens.categories

sealed class CategoryGroup(
  val id: String,
  val name: String,
) {
  data object Genre : CategoryGroup(
    id = "genre",
    name = "Genre",
  )

  data object Theme : CategoryGroup(
    id = "theme",
    name = "Theme",
  )

  data object Format : CategoryGroup(
    id = "format",
    name = "Format",
  )

  data object Content : CategoryGroup(
    id = "content",
    name = "Content",
  )
}