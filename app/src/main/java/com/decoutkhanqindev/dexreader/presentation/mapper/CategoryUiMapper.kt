package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.Category
import com.decoutkhanqindev.dexreader.presentation.model.CategoryUiModel

object CategoryUiMapper {
  fun Category.toCategoryUiModel(): CategoryUiModel = CategoryUiModel(
    id = id,
    title = title,
  )
}
