package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.category.Category
import com.decoutkhanqindev.dexreader.presentation.model.CategoryUiModel

object CategoryUiMapper {
  fun Category.toCategoryUiModel() = CategoryUiModel(id = id, title = title)
}
