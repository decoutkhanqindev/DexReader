package com.decoutkhanqindev.dexreader.presentation.screens.statistics

import androidx.compose.runtime.Immutable

@Immutable
data class StatisticsUiState(
  val dailyTimeMillis: Long = 0,
  val weeklyTimeMillis: Long = 0,
  val totalTimeMillis: Long = 0,
  val isLoading: Boolean = false,
)