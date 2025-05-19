package com.decoutkhanqindev.dexreader.domain.usecase.manga

import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.repository.MangaRepository
import javax.inject.Inject

class GetNewReleaseMangaListUseCase @Inject constructor(
  private val mangaRepository: MangaRepository
) {
  suspend operator fun invoke(): Result<List<Manga>> {
    return mangaRepository.getLatestUploadedMangaList()
  }
}