package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.TagDto
import com.decoutkhanqindev.dexreader.domain.model.Category

fun TagDto.toDomain(): Category =
  Category(
    id = id,
    name = attributes.name?.get("en") ?: "Unknown",
    description = attributes.description?.get("en") ?: "No descriptions ..."
  )