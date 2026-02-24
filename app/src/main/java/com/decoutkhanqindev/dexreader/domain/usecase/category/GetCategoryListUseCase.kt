package com.decoutkhanqindev.dexreader.domain.usecase.category

import com.decoutkhanqindev.dexreader.domain.model.Category
import com.decoutkhanqindev.dexreader.domain.repository.CategoryRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class GetCategoryListUseCase @Inject constructor(
  private val repository: CategoryRepository,
) {
  suspend operator fun invoke(): Result<List<Category>> =
    runSuspendResultCatching {
      repository.getCategoryList()
    }
}