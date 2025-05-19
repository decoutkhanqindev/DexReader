package com.decoutkhanqindev.dexreader.domain.usecase.manga

import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.repository.MangaRepository
import jakarta.inject.Inject

class GetChapterListUseCase @Inject constructor(
  private val mangaRepository: MangaRepository,
) {
  suspend operator fun invoke(mangaId: String): Result<List<Chapter>> =
    mangaRepository.getChapterList(mangaId)
}