package com.decoutkhanqindev.dexreader.presentation.ui.categories

sealed class CategoryGroup(
  val id: String,
  val name: String,
) {
  object Genre : CategoryGroup(
    id = "genre",
    name = "Genre",
  )

  object Theme : CategoryGroup(
    id = "theme",
    name = "Theme",
  )

  object Format : CategoryGroup(
    id = "theme",
    name = "Format",
  )

  object Content : CategoryGroup(
    id = "theme",
    name = "Content",
  )
}