package com.decoutkhanqindev.dexreader.presentation.ui.common.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.navigation.NavigationDestination

@Composable
fun MenuDrawer(
  drawerState: DrawerState,
  selectedItemId: String,
  onItemClick: (String) -> Unit,
  content: @Composable () -> Unit,
  modifier: Modifier = Modifier,
) {
  val items = listOf(
    MenuItem(
      id = NavigationDestination.HomeScreen.route,
      title = stringResource(R.string.home_menu_item),
      icon = Icons.Default.Home
    ),
    MenuItem(
      id = NavigationDestination.CategoriesScreen.route,
      title = stringResource(R.string.categories_menu_item),
      icon = Icons.Default.Category
    ),
    MenuItem(
      id = NavigationDestination.FavoriteScreen.route,
      title = stringResource(R.string.favorite_menu_item),
      icon = Icons.Default.Favorite
    ),
    MenuItem(
      id = NavigationDestination.HistoryScreen.route,
      title = stringResource(R.string.history_menu_item),
      icon = Icons.Default.History
    ),
    MenuItem(
      id = NavigationDestination.ProfileScreen.route,
      title = stringResource(R.string.profile_menu_item),
      icon = Icons.Default.Person
    ),
    MenuItem(
      id = NavigationDestination.SettingsScreen.route,
      title = stringResource(R.string.settings_menu_item),
      icon = Icons.Default.Settings
    )
  )

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      ModalDrawerSheet {
        Column(modifier = Modifier.fillMaxSize()) {
          MenuHeader(
            modifier = Modifier
              .weight(0.4f)
              .fillMaxWidth()
              .background(color = MaterialTheme.colorScheme.surfaceContainer)
              .padding(16.dp)
          )
          MenuBody(
            items = items,
            selectedItemId = selectedItemId,
            onItemClick = onItemClick,
            modifier = Modifier
              .weight(2f)
              .fillMaxWidth()
          )
          MenuFooter(
            modifier = Modifier
              .weight(0.2f)
              .fillMaxWidth()
              .background(color = MaterialTheme.colorScheme.surface)
          )
        }
      }
    },
    content = { content() },
    modifier = modifier
  )
}