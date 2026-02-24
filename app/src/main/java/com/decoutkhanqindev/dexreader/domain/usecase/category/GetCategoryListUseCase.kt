package com.decoutkhanqindev.dexreader.domain.usecase.category

import com.decoutkhanqindev.dexreader.domain.model.Category
import com.decoutkhanqindev.dexreader.domain.repository.CategoryRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import javax.inject.Inject

class GetCategoryListUseCase @Inject constructor(
  private val repository: CategoryRepository,
) {
  suspend operator fun invoke(): Result<List<Category>> =
    runSuspendCatching {
      repository.getCategoryList()
    }
}