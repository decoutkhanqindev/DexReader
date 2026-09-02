package com.decoutkhanqindev.dexreader.presentation.screens.profile.components.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.decoutkhanqindev.dexreader.presentation.screens.common.TestTags
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingChartPointModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.ListLoadingIndicator
import com.decoutkhanqindev.dexreader.presentation.screens.common.sections.SectionHeader
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadPageErrorMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.statistics.StatisticsUiState
import com.decoutkhanqindev.dexreader.presentation.screens.statistics.components.ReadingActivityChart
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ProfileStatisticsSection(
  uiState: StatisticsUiState,
  modifier: Modifier = Modifier,
  onRetry: () -> Unit,
  onMoreClick: (() -> Unit)? = null,
) {
  Column(modifier = modifier) {
    SectionHeader(
      icon = Icons.Default.Timeline,
      title = stringResource(R.string.statistics_menu_item),
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          start = 16.dp,
          end = 16.dp,
          top = 8.dp,
        ),
      onMoreClick = onMoreClick
    )

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(300.dp),
      contentAlignment = Alignment.Center
    ) {
      when (uiState) {
        StatisticsUiState.Loading -> ListLoadingIndicator(
          modifier = Modifier.fillMaxWidth()
        )

        is StatisticsUiState.Error -> LoadPageErrorMessage(
          message = stringResource(uiState.error.messageRes),
          onRetryClick = onRetry,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        )

        is StatisticsUiState.Success -> ReadingActivityChart(
          dataPoints = uiState.monthlyBreakdown,
          modifier = Modifier
            .fillMaxSize()
            .testTag(TestTags.PROFILE_STATISTICS_CHART)
            .padding(horizontal = 16.dp)
        )
      }
    }
  }
}

@Preview
@Composable
private fun ProfileStatisticsSectionContentPreview() {
  DexReaderTheme {
    ProfileStatisticsSection(
      uiState = StatisticsUiState.Success(
        monthlyBreakdown = persistentListOf(
          ReadingChartPointModel(id = "2026-06", label = "Jun", minutes = 320),
          ReadingChartPointModel(id = "2026-07", label = "Jul", minutes = 540),
          ReadingChartPointModel(id = "2026-08", label = "Aug", minutes = 210),
        ),
      ),
      modifier = Modifier.fillMaxWidth(),
      onMoreClick = {},
      onRetry = {}
    )
  }
}

@Preview
@Composable
private fun ProfileStatisticsSectionErrorPreview() {
  DexReaderTheme {
    ProfileStatisticsSection(
      uiState = StatisticsUiState.Error(FeatureError.NetworkUnavailable),
      modifier = Modifier.fillMaxWidth(),
      onMoreClick = {},
      onRetry = {}
    )
  }
}
