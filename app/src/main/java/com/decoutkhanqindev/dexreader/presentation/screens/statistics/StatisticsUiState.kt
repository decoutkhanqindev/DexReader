package com.decoutkhanqindev.dexreader.presentation.screens.statistics

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError

@Immutable
sealed interface StatisticsUiState {
  data object Loading : StatisticsUiState

  @Immutable
  data class Success(
    val dailyTimeMillis: Long = 0,
    val weeklyTimeMillis: Long = 0,
    val totalTimeMillis: Long = 0,
  ) : StatisticsUiState

  @Immutable
  data class Error(val error: FeatureError = FeatureError.Generic) : StatisticsUiState
}
