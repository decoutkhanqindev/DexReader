package com.decoutkhanqindev.dexreader.presentation.screens.statistics.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.statistics.StatisticsUiState
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun StatisticsContent(
  uiState: StatisticsUiState,
  modifier: Modifier = Modifier,
  onRetry: () -> Unit,
) {
  var isShowErrorDialog by remember { mutableStateOf(false) }

  LaunchedEffect(uiState) {
    if (uiState is StatisticsUiState.Error) isShowErrorDialog = true
  }

  when (uiState) {
    StatisticsUiState.Loading -> LoadingScreen(modifier = modifier)

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
        modifier = modifier
          .fillMaxSize()
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = stringResource(R.string.statistics_menu_item),
          style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))

        StatCard(
          label = stringResource(R.string.daily_reading_time),
          millis = uiState.dailyTimeMillis
        )
        Spacer(modifier = Modifier.height(16.dp))
        StatCard(
          label = stringResource(R.string.weekly_reading_time),
          millis = uiState.weeklyTimeMillis
        )
        Spacer(modifier = Modifier.height(16.dp))
        StatCard(
          label = stringResource(R.string.total_reading_time),
          millis = uiState.totalTimeMillis
        )
      }
    }
  }
}

@Preview
@Composable
private fun StatisticsContentLoadingPreview() {
  DexReaderTheme {
    StatisticsContent(
      uiState = StatisticsUiState.Loading,
      modifier = Modifier.fillMaxSize(),
      onRetry = {}
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
      onRetry = {}
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
      ),
      modifier = Modifier.fillMaxSize(),
      onRetry = {}
    )
  }
}
