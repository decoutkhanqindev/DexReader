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
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.menu.MenuDrawer
import com.decoutkhanqindev.dexreader.presentation.screens.common.top_bars.MainTopBar
import kotlinx.coroutines.launch

@Composable
fun BaseScreen(
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  onNavigateToSignInScreen: () -> Unit,
  selectedMenuItem: MenuItemValue,
  onNavigateToMenuItemScreen: (MenuItemValue) -> Unit,
  isSearchEnabled: Boolean = true,
  onNavigateToSearchScreen: () -> Unit = {},
  bottomBar: @Composable () -> Unit = {},
  content: @Composable () -> Unit,
  modifier: Modifier = Modifier,
) {
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val coroutineScope = rememberCoroutineScope()

  MenuDrawer(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    onSignInClick = onNavigateToSignInScreen,
    drawerState = drawerState,
    selectedItem = selectedMenuItem,
    onItemClick = {
      coroutineScope.launch {
        drawerState.close()
        onNavigateToMenuItemScreen(it)
      }
    },
    content = {
      Scaffold(
        topBar = {
          MainTopBar(
            title = stringResource(selectedMenuItem.nameRes),
            onNavigateToMenuItemScreen = {
              coroutineScope.launch {
                drawerState.open()
              }
            },
            isSearchEnabled = isSearchEnabled,
            onNavigateToSignInScreen = onNavigateToSearchScreen,
            modifier = Modifier.fillMaxWidth()
          )
        },
        bottomBar = bottomBar,
        content = { paddingValues ->
          Box(
            modifier = Modifier
              .padding(paddingValues)
              .fillMaxSize()
          ) { content() }
        }
      )
    },
    modifier = modifier,
  )
}