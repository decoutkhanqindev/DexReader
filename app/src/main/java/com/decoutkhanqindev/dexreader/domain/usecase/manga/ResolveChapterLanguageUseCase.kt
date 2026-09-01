package com.decoutkhanqindev.dexreader.domain.usecase.manga

import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.domain.repository.manga.ChapterRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.runSuspendResultCatching
import javax.inject.Inject

class ResolveChapterLanguageUseCase @Inject constructor(
  private val repository: ChapterRepository,
) {
  suspend operator fun invoke(mangaId: String): Result<MangaLanguage> =
    runSuspendResultCatching { repository.resolveChapterLanguage(mangaId = mangaId) }
}
