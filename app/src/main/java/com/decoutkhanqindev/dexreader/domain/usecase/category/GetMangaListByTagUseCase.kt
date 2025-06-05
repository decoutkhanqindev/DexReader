package com.decoutkhanqindev.dexreader.domain.usecase.category

import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.repository.CategoryRepository
import javax.inject.Inject

class GetMangaListByTagUseCase @Inject constructor(
  private val categoryRepository: CategoryRepository
) {
  suspend operator fun invoke(
    tagId: String,
    offset: Int = 0
  ): Result<List<Manga>> =
    categoryRepository.getMangaListByTag(
      tagId = tagId,
      offset = offset
    )
}