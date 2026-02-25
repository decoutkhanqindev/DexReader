package com.decoutkhanqindev.dexreader.data.network.mangadex_api.request

enum class MangaSortCriteriaParam(val value: String) {
  LATEST_UPDATE("updatedAt"),
  TRENDING("followedCount"),
  MOST_VIEWED("createdAt"), // Follows the API logic as new release
  TOP_RATED("rating")
}
