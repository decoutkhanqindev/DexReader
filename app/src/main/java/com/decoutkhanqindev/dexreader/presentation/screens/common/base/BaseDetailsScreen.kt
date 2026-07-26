package com.decoutkhanqindev.dexreader.presentation.screens.common.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.decoutkhanqindev.dexreader.presentation.screens.common.top_bars.AppTopBar

@Composable
fun BaseDetailsScreen(
  title: String,
  isSearchEnabled: Boolean = true,
  modifier: Modifier = Modifier,
  onNavigateBack: () -> Unit,
  onNavigateToSearchScreen: () -> Unit = {},
  bottomBar: @Composable () -> Unit = {},
  content: @Composable () -> Unit,
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      AppTopBar(
        leftIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onLeftClick = onNavigateBack,
        centerTitle = title,
        rightIcon = if (isSearchEnabled) Icons.Default.Search else null,
        onRightClick = onNavigateToSearchScreen,
        modifier = Modifier.fillMaxWidth(),
      )
    },
    bottomBar = bottomBar,
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) { content() }
  }
}