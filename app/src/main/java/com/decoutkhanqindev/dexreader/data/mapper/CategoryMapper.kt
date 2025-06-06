package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.TagDto
import com.decoutkhanqindev.dexreader.domain.model.Category

fun TagDto.toDomain(): Category =
  Category(
    id = id,
    title = attributes.name?.get("en") ?: "Unknown",
    group = attributes.group ?: "Unknown"
  )