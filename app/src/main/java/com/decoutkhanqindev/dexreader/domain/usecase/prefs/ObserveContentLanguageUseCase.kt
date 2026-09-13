package com.decoutkhanqindev.dexreader.domain.usecase.prefs

import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.domain.repository.prefs.PrefsRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.toFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveContentLanguageUseCase @Inject constructor(
  private val repository: PrefsRepository,
) {
  operator fun invoke(): Flow<Result<MangaLanguage>> =
    repository.observeContentLanguage().toFlowResult()
}
