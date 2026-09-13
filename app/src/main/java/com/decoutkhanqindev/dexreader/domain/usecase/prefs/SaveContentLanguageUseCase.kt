package com.decoutkhanqindev.dexreader.domain.usecase.prefs

import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.domain.repository.prefs.PrefsRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.runSuspendResultCatching
import javax.inject.Inject

class SaveContentLanguageUseCase @Inject constructor(
  private val repository: PrefsRepository,
) {
  suspend operator fun invoke(value: MangaLanguage): Result<Unit> =
    runSuspendResultCatching { repository.saveContentLanguage(value) }
}
