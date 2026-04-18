package com.decoutkhanqindev.dexreader.presentation.screens.common.base

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
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.menu.MenuDrawer
import com.decoutkhanqindev.dexreader.presentation.screens.common.top_bars.MainTopBar
import kotlinx.coroutines.launch

@Composable
fun BaseScreen(
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  selectedMenuItem: MenuValue,
  isSearchEnabled: Boolean = true,
  modifier: Modifier = Modifier,
  onNavigateToSignInScreen: () -> Unit,
  onNavigateToMenuItemScreen: (MenuValue) -> Unit,
  onNavigateToSearchScreen: () -> Unit = {},
  bottomBar: @Composable () -> Unit = {},
  content: @Composable () -> Unit,
) {
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val coroutineScope = rememberCoroutineScope()

  MenuDrawer(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    drawerState = drawerState,
    selectedItem = selectedMenuItem,
    modifier = modifier,
    onSignInClick = onNavigateToSignInScreen,
    onItemClick = {
      coroutineScope.launch {
        drawerState.close()
        onNavigateToMenuItemScreen(it)
      }
    }
  ) {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
        MainTopBar(
          title = stringResource(selectedMenuItem.nameRes),
          isSearchEnabled = isSearchEnabled,
          modifier = Modifier.fillMaxWidth(),
          onNavigateToMenuItemScreen = {
            coroutineScope.launch {
              drawerState.open()
            }
          },
          onNavigateToSignInScreen = onNavigateToSearchScreen
        )
      },
      bottomBar = bottomBar
    ) { paddingValues ->
      Box(
        modifier = Modifier
          .padding(paddingValues)
          .fillMaxSize()
      ) { content() }
    }
  }
}