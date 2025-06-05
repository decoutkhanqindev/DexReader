package com.decoutkhanqindev.dexreader.domain.usecase.tag

import com.decoutkhanqindev.dexreader.domain.model.Tag
import com.decoutkhanqindev.dexreader.domain.repository.TagRepository
import javax.inject.Inject

class GetTagListUseCase @Inject constructor(
  private val categoryRepository: TagRepository
) {
  suspend operator fun invoke(): Result<List<Tag>> =
    categoryRepository.getTagList()
}