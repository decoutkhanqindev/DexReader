package com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.statistics

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingChartPointModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
sealed interface StatisticsUiState {
  data object Loading : StatisticsUiState

  @Immutable
  data class Success(
    val dailyTimeMillis: Long = 0,
    val weeklyTimeMillis: Long = 0,
    val totalTimeMillis: Long = 0,
    val weeklyBreakdown: ImmutableList<ReadingChartPointModel> = persistentListOf(),
    val monthlyBreakdown: ImmutableList<ReadingChartPointModel> = persistentListOf(),
    val yearlyBreakdown: ImmutableList<ReadingChartPointModel> = persistentListOf(),
  ) : StatisticsUiState

  @Immutable
  data class Error(val error: FeatureError = FeatureError.Generic) : StatisticsUiState
}
