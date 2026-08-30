package com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.statistics

import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingStats
import com.decoutkhanqindev.dexreader.domain.usecase.user.statistics.ObserveStatisticsUseCase
import com.decoutkhanqindev.dexreader.presentation.mapper.ErrorMapper.toFeatureError
import com.decoutkhanqindev.dexreader.presentation.mapper.StatisticsMapper.toMonthlyChartPoint
import com.decoutkhanqindev.dexreader.presentation.mapper.StatisticsMapper.toWeeklyChartPoint
import com.decoutkhanqindev.dexreader.presentation.mapper.StatisticsMapper.toYearlyChartPoint
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
  private val observeStatisticsUseCase: ObserveStatisticsUseCase,
) : BaseViewModel() {

  private val _uiState = MutableStateFlow<StatisticsUiState>(StatisticsUiState.Loading)
  val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

  private val _userId = MutableStateFlow<String?>(null)

  init {
    observeStatistics()
  }

  fun updateUserId(userId: String?) {
    _userId.value = userId
  }

  private fun observeStatistics() {
    vmLaunch {
      _userId.collectLatest { userId ->
        if (userId == null) {
          _uiState.value = StatisticsUiState.Loading
          return@collectLatest
        }

        _uiState.value = StatisticsUiState.Loading

        observeStatisticsUseCase(userId).collect { result ->
          result.onSuccess { statsList -> calculateStats(statsList) }
          result.onFailure { throwable ->
            _uiState.value = StatisticsUiState.Error(throwable.toFeatureError())
            Timber.tag(this::class.java.simpleName)
              .e("observeStatistics have error: ${throwable.stackTraceToString()}")
          }
        }
      }
    }
  }

  private fun calculateStats(statsList: List<ReadingStats>) {
    val today = ReadingStats.getCurrentDate()
    val weeklyStats = ReadingStats.buildWeeklyBreakdown(statsList)
    val monthlyStats = ReadingStats.buildMonthlyBreakdown(statsList)
    val yearlyStats = ReadingStats.buildYearlyBreakdown(statsList)
    val dailyTime = statsList.find { it.date == today }?.durationMillis ?: 0L
    val weeklyTime = weeklyStats.sumOf { it.durationMillis }
    val totalTime = statsList.sumOf { it.durationMillis }

    _uiState.value = StatisticsUiState.Success(
      dailyTimeMillis = dailyTime,
      weeklyTimeMillis = weeklyTime,
      totalTimeMillis = totalTime,
      weeklyBreakdown = weeklyStats.map { it.toWeeklyChartPoint() }.toImmutableList(),
      monthlyBreakdown = monthlyStats.map { it.toMonthlyChartPoint() }.toImmutableList(),
      yearlyBreakdown = yearlyStats.map { it.toYearlyChartPoint() }.toImmutableList(),
    )
  }

  fun refresh() {
    observeStatistics()
  }

  fun retry() {
    if (_uiState.value is StatisticsUiState.Error) observeStatistics()
  }
}
