package com.decoutkhanqindev.dexreader.presentation.screens.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingChartPointModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.statistics.StatisticsUiState
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsContent(
  uiState: StatisticsUiState,
  modifier: Modifier = Modifier,
  onRetry: () -> Unit,
  onRefresh: () -> Unit,
) {
  var isShowErrorDialog by remember { mutableStateOf(false) }
  val pullToRefreshState = rememberPullToRefreshState()

  SideEffect(uiState) {
    if (uiState is StatisticsUiState.Error) isShowErrorDialog = true
  }

  PullToRefreshBox(
    state = pullToRefreshState,
    isRefreshing = false,
    onRefresh = onRefresh,
    modifier = modifier,
  ) {
    when (uiState) {
      StatisticsUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())

      is StatisticsUiState.Error -> {
        if (isShowErrorDialog) {
          AlertDialog(
            title = stringResource(uiState.error.messageRes),
            onConfirmClick = {
              isShowErrorDialog = false
              onRetry()
            },
            onDismissClick = { isShowErrorDialog = false },
          )
        }
      }

      is StatisticsUiState.Success -> {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = stringResource(R.string.weekly_reading_activity),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 8.dp)
          )
          Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = stringResource(R.string.daily_reading_time) + ": " +
                formatDuration(uiState.dailyTimeMillis),
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = stringResource(R.string.weekly_reading_time) + ": " +
                formatDuration(uiState.weeklyTimeMillis),
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Spacer(modifier = Modifier.height(12.dp))
          ReadingActivityChart(
            dataPoints = uiState.weeklyBreakdown,
            modifier = Modifier
              .fillMaxWidth()
              .height(300.dp)
          )
          Spacer(modifier = Modifier.height(28.dp))

          Text(
            text = stringResource(R.string.monthly_reading_activity),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 8.dp)
          )
          Text(
            text = stringResource(R.string.total_reading_time) + ": " +
              formatDuration(uiState.totalTimeMillis),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(12.dp))
          ReadingActivityChart(
            dataPoints = uiState.monthlyBreakdown,
            modifier = Modifier
              .fillMaxWidth()
              .height(300.dp)
          )
          Spacer(modifier = Modifier.height(28.dp))

          Text(
            text = stringResource(R.string.yearly_reading_activity),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 8.dp)
          )
          Text(
            text = stringResource(R.string.total_reading_time) + ": " +
              formatDuration(uiState.totalTimeMillis),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(12.dp))
          ReadingActivityChart(
            dataPoints = uiState.yearlyBreakdown,
            modifier = Modifier
              .fillMaxWidth()
              .height(300.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun formatDuration(millis: Long): String {
  val minutes = (millis / 60_000).toInt()
  val hours = minutes / 60
  val remainingMinutes = minutes % 60
  return if (hours > 0) {
    stringResource(R.string.hours_suffix, hours) + " " +
      stringResource(R.string.minutes_suffix, remainingMinutes)
  } else {
    stringResource(R.string.minutes_suffix, minutes)
  }
}

@Preview
@Composable
private fun StatisticsContentLoadingPreview() {
  DexReaderTheme {
    StatisticsContent(
      uiState = StatisticsUiState.Loading,
      modifier = Modifier.fillMaxSize(),
      onRetry = {},
      onRefresh = {}
    )
  }
}

@Preview
@Composable
private fun StatisticsContentErrorPreview() {
  DexReaderTheme {
    StatisticsContent(
      uiState = StatisticsUiState.Error(FeatureError.NetworkUnavailable),
      modifier = Modifier.fillMaxSize(),
      onRetry = {},
      onRefresh = {}
    )
  }
}

@Preview
@Composable
private fun StatisticsContentSuccessPreview() {
  DexReaderTheme {
    StatisticsContent(
      uiState = StatisticsUiState.Success(
        dailyTimeMillis = 5_400_000L,
        weeklyTimeMillis = 27_000_000L,
        totalTimeMillis = 108_000_000L,
        weeklyBreakdown = persistentListOf(
          ReadingChartPointModel(id = "2026-08-18", label = "Mon", minutes = 12),
          ReadingChartPointModel(id = "2026-08-19", label = "Tue", minutes = 25),
          ReadingChartPointModel(id = "2026-08-20", label = "Wed", minutes = 0),
          ReadingChartPointModel(id = "2026-08-21", label = "Thu", minutes = 40),
          ReadingChartPointModel(id = "2026-08-22", label = "Fri", minutes = 18),
          ReadingChartPointModel(id = "2026-08-23", label = "Sat", minutes = 33),
          ReadingChartPointModel(id = "2026-08-24", label = "Sun", minutes = 22),
        ),
        monthlyBreakdown = persistentListOf(
          ReadingChartPointModel(id = "2026-06", label = "Jun", minutes = 320),
          ReadingChartPointModel(id = "2026-07", label = "Jul", minutes = 540),
          ReadingChartPointModel(id = "2026-08", label = "Aug", minutes = 210),
        ),
        yearlyBreakdown = persistentListOf(
          ReadingChartPointModel(id = "2024", label = "2024", minutes = 4_200),
          ReadingChartPointModel(id = "2025", label = "2025", minutes = 6_800),
          ReadingChartPointModel(id = "2026", label = "2026", minutes = 1_070),
        ),
      ),
      modifier = Modifier.fillMaxSize(),
      onRetry = {},
      onRefresh = {}
    )
  }
}
