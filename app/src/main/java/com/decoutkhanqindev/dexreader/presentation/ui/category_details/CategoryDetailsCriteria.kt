package com.decoutkhanqindev.dexreader.presentation.ui.category_details

sealed class SortCriteria(
  val id: String,
  val name: String,
) {
  data object LatestUpdate : SortCriteria(
    id = "latest_update",
    name = "Latest Update",
  )

  data object Trending : SortCriteria(
    id = "trending",
    name = "Trending",
  )

  data object NewReleases : SortCriteria(
    id = "new_releases",
    name = "New Releases",
  )

  data object TopRated : SortCriteria(
    id = "top_rated",
    name = "Top Rated",
  )
}


sealed class SortOrder(
  val id: String,
  val name: String,
) {
  data object Ascending : SortOrder(
    id = "asc",
    name = "Ascending",
  )

  data object Descending : SortOrder(
    id = "desc",
    name = "Descending",
  )
}

sealed class FilterCriteria(
  val id: String,
  val name: String,
) {
  data object Status : FilterCriteria(
    id = "status",
    name = "Status",
  )

  data object ContentRating : FilterCriteria(
    id = "content_rating",
    name = "Content Rating",
  )
}

sealed class FilterValue(
  val name: String,
  val id: String
) {
  data object Ongoing : FilterValue(
    id = "ongoing",
    name = "Ongoing",
  )

  data object Completed : FilterValue(
    id = "completed",
    name = "Completed",
  )

  data object Hiatus : FilterValue(
    id = "hiatus",
    name = "Hiatus",
  )

  data object Cancelled : FilterValue(
    id = "cancelled",
    name = "Cancelled",
  )

  data object Safe : FilterValue(
    id = "safe",
    name = "Safe",
  )

  data object Suggestive : FilterValue(
    id = "suggestive",
    name = "Suggestive",
  )

  data object Erotica : FilterValue(
    id = "erotica",
    name = "Erotica",
  )
}