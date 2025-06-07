package com.decoutkhanqindev.dexreader.presentation.ui.common.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.decoutkhanqindev.dexreader.presentation.ui.common.menu.MenuDrawer
import com.decoutkhanqindev.dexreader.presentation.ui.common.top_bars.MainTopBar
import kotlinx.coroutines.launch

@Composable
fun BaseScreen(
  title: String,
  route: String,
  onMenuItemClick: (String) -> Unit,
  isSearchEnabled: Boolean = true,
  onSearchClick: () -> Unit = {},
  content: @Composable () -> Unit,
  modifier: Modifier = Modifier
) {
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val coroutineScope = rememberCoroutineScope()

  MenuDrawer(
    drawerState = drawerState,
    selectedItemId = route,
    onItemClick = { route ->
      coroutineScope.launch {
        drawerState.close()
        onMenuItemClick(route)
      }
    },
    content = {
      Scaffold(
        topBar = {
          MainTopBar(
            title = title,
            onMenuClick = {
              coroutineScope.launch {
                drawerState.open()
              }
            },
            isSearchEnabled = isSearchEnabled,
            onSearchClick = onSearchClick,
            modifier = Modifier.fillMaxWidth()
          )
        },
        content = { paddingValues ->
          Box(
            modifier = Modifier
              .padding(paddingValues)
              .fillMaxSize()
          ) {
            content()
          }
        }
      )
    },
    modifier = modifier,
  )
}