package com.decoutkhanqindev.dexreader.presentation.screens.common.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.dexreader.presentation.model.value.bottom_bar.BottomTabItemValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.top_bars.AppTopBar

@Composable
fun BaseScreen(
  selectedTab: BottomTabItemValue,
  isSearchEnabled: Boolean = true,
  isSettingsEnabled: Boolean = false,
  modifier: Modifier = Modifier,
  onNavigateToSearchScreen: () -> Unit = {},
  onNavigateToSettingsScreen: () -> Unit = {},
  content: @Composable () -> Unit,
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      AppTopBar(
        centerTitle = stringResource(selectedTab.nameRes),
        rightIcon = when {
          isSearchEnabled -> Icons.Default.Search
          isSettingsEnabled -> Icons.Default.Settings
          else -> null
        },
        onRightClick = if (isSearchEnabled) onNavigateToSearchScreen
        else onNavigateToSettingsScreen,
        modifier = Modifier.fillMaxWidth(),
      )
    },
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
    ) { content() }
  }
}
