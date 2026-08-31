package com.decoutkhanqindev.dexreader.presentation.screens.common.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.dexreader.presentation.model.value.bottom_bar.BottomTabItemValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.top_bars.AppTopBar

@Composable
fun BaseScreen(
  selectedTab: BottomTabItemValue,
  isSearchEnabled: Boolean = true,
  modifier: Modifier = Modifier,
  onNavigateToSearchScreen: () -> Unit = {},
  content: @Composable () -> Unit,
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      AppTopBar(
        centerTitle = stringResource(selectedTab.nameRes),
        rightIcon = if (isSearchEnabled) Icons.Default.Search else null,
        onRightClick = onNavigateToSearchScreen,
        containerColor = Color.Transparent,
        centerContentColor = MaterialTheme.colorScheme.onSurface,
        rightContentColor = MaterialTheme.colorScheme.primary,
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
