package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.tag.TagResponse
import com.decoutkhanqindev.dexreader.domain.model.Category

fun TagResponse.toCategory(): Category =
  Category(
    id = id,
    title = attributes?.name?.get("en") ?: attributes?.name?.values?.firstOrNull() ?: "Unknown",
    group = attributes?.group ?: "Unknown"
  )