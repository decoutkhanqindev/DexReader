package com.decoutkhanqindev.dexreader.presentation.ui.categories

sealed class CategoryGroup(
  val  name: String,
  val value: String
) {
  object Genre : CategoryGroup(
    name = "Genre",
    value = "genre"
  )

  object Theme : CategoryGroup(
    name = "Theme",
    value = "theme"
  )

  object Format : CategoryGroup(
    name = "Format",
    value = "format"
  )

  object Content : CategoryGroup(
    name = "Content",
    value = "content"
  )
}