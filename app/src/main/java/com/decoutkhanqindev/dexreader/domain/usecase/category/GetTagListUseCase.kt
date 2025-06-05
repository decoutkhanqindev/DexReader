package com.decoutkhanqindev.dexreader.domain.usecase.category

import com.decoutkhanqindev.dexreader.domain.model.Category
import com.decoutkhanqindev.dexreader.domain.repository.CategoryRepository
import javax.inject.Inject

class GetTagListUseCase @Inject constructor(
  private val categoryRepository: CategoryRepository
) {
  suspend operator fun invoke(): Result<List<Category>> =
    categoryRepository.getTagList()
}