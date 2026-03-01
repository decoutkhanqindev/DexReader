package com.decoutkhanqindev.dexreader.domain.usecase.manga

import com.decoutkhanqindev.dexreader.domain.repository.MangaRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class GetMangaSuggestionsUseCase @Inject constructor(
  private val repository: MangaRepository,
) {
  suspend operator fun invoke(query: String): Result<List<String>> =
    runSuspendResultCatching {
      repository.searchManga(query = query, offset = 0)
        .take(SUGGESTION_LIMIT)
        .map { it.title }
    }

  private companion object {
    const val SUGGESTION_LIMIT = 10
  }
}
