package com.decoutkhanqindev.dexreader.domain.usecase.category

import com.decoutkhanqindev.dexreader.domain.entity.category.Category
import com.decoutkhanqindev.dexreader.domain.repository.category.CategoryRepository
import com.decoutkhanqindev.dexreader.domain.entity.value.category.CategoryType
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class GetCategoryListUseCase @Inject constructor(
  private val repository: CategoryRepository,
) {
  suspend operator fun invoke(): Result<Map<CategoryType, List<Category>>> =
    runSuspendResultCatching { repository.getCategoryList().groupBy { it.type } }
}