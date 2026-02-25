package com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.manga

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaDetailsResponse(@field:Json(name = "data") val data: MangaResponse? = null)
