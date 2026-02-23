package com.decoutkhanqindev.dexreader.domain.usecase.manga

import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.repository.MangaRepository
import com.decoutkhanqindev.dexreader.utils.AsyncHandler.runSuspendCatching
import javax.inject.Inject

class SearchMangaUseCase @Inject constructor(
  private val repository: MangaRepository,
) {
  suspend operator fun invoke(query: String, offset: Int = 0): Result<List<Manga>> =
    runSuspendCatching {
      repository.searchManga(query = query, offset = offset)
    }
}
