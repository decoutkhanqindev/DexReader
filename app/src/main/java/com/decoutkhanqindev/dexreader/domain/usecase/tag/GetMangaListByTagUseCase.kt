package com.decoutkhanqindev.dexreader.domain.usecase.tag

import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.repository.TagRepository
import javax.inject.Inject

class GetMangaListByTagUseCase @Inject constructor(
  private val categoryRepository: TagRepository
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