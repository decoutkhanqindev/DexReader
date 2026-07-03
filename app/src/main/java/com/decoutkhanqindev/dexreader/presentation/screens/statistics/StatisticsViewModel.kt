package com.decoutkhanqindev.dexreader.presentation.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingStats
import com.decoutkhanqindev.dexreader.domain.usecase.user.statistics.ObserveStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
  private val observeStatisticsUseCase: ObserveStatisticsUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(StatisticsUiState())
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
        Timber.tag("StatisticsDebug").d("ViewModel user changed: $userId")
        if (userId == null) {
          _uiState.update { it.copy(dailyTimeMillis = 0, weeklyTimeMillis = 0, totalTimeMillis = 0) }
          return@collectLatest
        }

        observeStatisticsUseCase(userId).collect { result ->
          result.onSuccess { statsList ->
            Timber.tag("StatisticsDebug").d("ViewModel received stats list: ${statsList.size} items")
            calculateStats(statsList)
          }
          result.onFailure {
            Timber.tag("StatisticsDebug").e(it, "UseCase failed")
          }
        }
      }
    }
  }

  private fun calculateStats(statsList: List<ReadingStats>) {
    val today = ReadingStats.getCurrentDate()
    Timber.tag("StatisticsDebug").d("Calculating stats. Today is: $today")
    
    val dailyTime = statsList.find { it.date == today }?.durationMillis ?: 0L
    Timber.tag("StatisticsDebug").d("Found daily time: $dailyTime")
    
    val totalTime = statsList.sumOf { it.durationMillis }
    Timber.tag("StatisticsDebug").d("Total time sum: $totalTime")
    
    _uiState.update {
      it.copy(
        dailyTimeMillis = dailyTime,
        weeklyTimeMillis = totalTime,
        totalTimeMillis = totalTime
      )
    }
  }
}

data class StatisticsUiState(
  val dailyTimeMillis: Long = 0,
  val weeklyTimeMillis: Long = 0,
  val totalTimeMillis: Long = 0,
  val isLoading: Boolean = false,
)
