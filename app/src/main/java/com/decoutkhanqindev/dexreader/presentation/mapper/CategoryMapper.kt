package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.entity.category.Category
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel

object CategoryMapper {
  fun Category.toCategoryModel() = CategoryModel(id = id, title = title)
}
