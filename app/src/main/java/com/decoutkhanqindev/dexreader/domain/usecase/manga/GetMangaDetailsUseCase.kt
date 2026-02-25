package com.decoutkhanqindev.dexreader.domain.usecase.manga

import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.repository.MangaRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class GetMangaDetailsUseCase @Inject constructor(
  private val repository: MangaRepository,
) {
  suspend operator fun invoke(mangaId: String): Result<Manga> =
    runSuspendResultCatching { repository.getMangaDetails(mangaId) }
}
