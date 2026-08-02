package com.decoutkhanqindev.dexreader.presentation.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingStats
import com.decoutkhanqindev.dexreader.domain.usecase.user.statistics.ObserveStatisticsUseCase
import com.decoutkhanqindev.dexreader.presentation.mapper.ErrorMapper.toFeatureError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
  private val observeStatisticsUseCase: ObserveStatisticsUseCase,
) : ViewModel() {

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
    viewModelScope.launch {
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
    val dailyTime = statsList.find { it.date == today }?.durationMillis ?: 0L
    val totalTime = statsList.sumOf { it.durationMillis }

    _uiState.value = StatisticsUiState.Success(
      dailyTimeMillis = dailyTime,
      weeklyTimeMillis = totalTime,
      totalTimeMillis = totalTime
    )
  }

  fun retry() {
    if (_uiState.value is StatisticsUiState.Error) observeStatistics()
  }
}
