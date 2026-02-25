package com.decoutkhanqindev.dexreader.data.network.mangadex_api.request

enum class MangaStatusFilterParam(val value: String) {
  ON_GOING("ongoing"),
  COMPLETED("completed"),
  HIATUS("hiatus"),
  CANCELLED("cancelled")
}
