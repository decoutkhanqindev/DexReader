package com.decoutkhanqindev.dexreader.presentation.screens.common.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.decoutkhanqindev.dexreader.presentation.screens.common.top_bars.DetailsTopBar

@Composable
fun BaseDetailsScreen(
  title: String,
  onNavigateBack: () -> Unit,
  isSearchEnabled: Boolean = true,
  onSearchClick: () -> Unit = {},
  bottomBar: @Composable () -> Unit = {},
  content: @Composable () -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      DetailsTopBar(
        title = title,
        onNavigateBack = onNavigateBack,
        isSearchEnabled = isSearchEnabled,
        onSearchClick = onSearchClick,
        modifier = Modifier.fillMaxWidth()
      )
    },
    bottomBar = bottomBar,
    content = { paddingValues ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
      ) {
        content()
      }
    },
    modifier = modifier
  )
}