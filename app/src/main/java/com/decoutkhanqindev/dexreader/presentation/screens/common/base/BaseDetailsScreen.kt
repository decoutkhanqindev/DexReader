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
  title: String = "",
  isBackEnabled: Boolean = true,
  isSearchEnabled: Boolean = true,
  modifier: Modifier = Modifier,
  onNavigateBack: () -> Unit = {},
  onNavigateToSearchScreen: () -> Unit = {},
  bottomBar: @Composable () -> Unit = {},
  floatingActionButton: @Composable () -> Unit = {},
  topBar: (@Composable () -> Unit)? = null,
  content: @Composable () -> Unit,
) {
  Scaffold(
    modifier = modifier,
    topBar = topBar ?: {
      AppTopBar(
        leftIcon = if (isBackEnabled) Icons.AutoMirrored.Filled.ArrowBack else null,
        onLeftClick = onNavigateBack,
        centerTitle = title,
        rightIcon = if (isSearchEnabled) Icons.Default.Search else null,
        onRightClick = onNavigateToSearchScreen,
        modifier = Modifier.fillMaxWidth(),
      )
    },
    bottomBar = bottomBar,
    floatingActionButton = floatingActionButton,
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) { content() }
  }
}