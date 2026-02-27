package com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.request

enum class MangaStatusParam(val value: String) {
  ON_GOING("ongoing"),
  COMPLETED("completed"),
  HIATUS("hiatus"),
  CANCELLED("cancelled")
}
