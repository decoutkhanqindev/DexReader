package com.decoutkhanqindev.dexreader.presentation.screens.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen

@Composable
fun StatisticsScreen(
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  onNavigateToLoginScreen: () -> Unit,
  onNavigateToMenuItemScreen: (MenuValue) -> Unit,
  onNavigateToSearchScreen: () -> Unit,
  viewModel: StatisticsViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(currentUser?.id) {
    viewModel.updateUserId(currentUser?.id)
  }

  BaseScreen(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    selectedMenuItem = MenuValue.STATISTICS,
    onNavigateToSignInScreen = onNavigateToLoginScreen,
    onNavigateToMenuItemScreen = onNavigateToMenuItemScreen,
    onNavigateToSearchScreen = onNavigateToSearchScreen,
    modifier = modifier,
  ) {
    Column(
      modifier = Modifier
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

@Composable
fun StatCard(label: String, millis: Long) {
  val minutes = (millis / 60_000).toInt()
  val hours = minutes / 60
  val remainingMinutes = minutes % 60

  Card(
    modifier = Modifier.padding(8.dp)
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = label, style = MaterialTheme.typography.titleMedium)
      Text(
        text = if (hours > 0) {
          stringResource(R.string.hours_suffix, hours) + " " + stringResource(R.string.minutes_suffix, remainingMinutes)
        } else {
          stringResource(R.string.minutes_suffix, minutes)
        },
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary
      )
    }
  }
}
