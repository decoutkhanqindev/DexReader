package com.decoutkhanqindev.dexreader.presentation.ui.category_details

sealed class SortingCriteria(
  val name: String,
  val value: String
) {
  object Ascending : SortingCriteria(
    name = "Ascending",
    value = "asc"
  )

  object Descending : SortingCriteria(
    name = "Descending",
    value = "desc"
  )
}

sealed class FilteringCriteria(
  val name: String,
  val value: String
) {
  object Ongoing : FilteringCriteria(
    name = "Ongoing",
    value = "ongoing"
  )

  object Completed : FilteringCriteria(
    name = "Completed",
    value = "completed"
  )

  object Hiatus : FilteringCriteria(
    name = "Hiatus",
    value = "hiatus"
  )

  object Cancelled : FilteringCriteria(
    name = "Cancelled",
    value = "cancelled"
  )

  object Safe : FilteringCriteria(
    name = "Safe",
    value = "safe"
  )

  object Suggestive : FilteringCriteria(
    name = "Suggestive",
    value = "suggestive"
  )

  object Erotica : FilteringCriteria(
    name = "Erotica",
    value = "erotica"
  )
}