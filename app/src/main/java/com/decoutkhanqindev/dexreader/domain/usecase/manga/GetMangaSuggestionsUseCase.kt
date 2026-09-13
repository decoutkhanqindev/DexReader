package com.decoutkhanqindev.dexreader.domain.usecase.manga

import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.suspendRunCatching
import javax.inject.Inject

class GetMangaSuggestionsUseCase @Inject constructor(
  private val repository: MangaRepository,
) {
  suspend operator fun invoke(query: String): Result<List<String>> =
    suspendRunCatching {
      repository
        .searchManga(query = query, offset = 0, limit = SUGGESTION_LIMIT)
        .map { it.title }
    }

  companion object {
    private const val SUGGESTION_LIMIT = 10
  }
}
