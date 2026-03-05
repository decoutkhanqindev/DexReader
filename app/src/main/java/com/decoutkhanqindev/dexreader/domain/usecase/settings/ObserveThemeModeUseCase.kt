package com.decoutkhanqindev.dexreader.domain.usecase.settings

import com.decoutkhanqindev.dexreader.domain.model.ThemeMode
import com.decoutkhanqindev.dexreader.domain.repository.SettingsRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.toFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveThemeModeUseCase @Inject constructor(
  private val repository: SettingsRepository,
) {
  operator fun invoke(): Flow<Result<ThemeMode>> = repository.observeThemeMode().toFlowResult()
}