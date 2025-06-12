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
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.presentation.navigation.NavDestination

@Composable
fun MenuDrawer(
  isUserLoggedIn: Boolean,
  currentUser: User?,
  onSignInClick: () -> Unit,
  drawerState: DrawerState,
  selectedItemId: String,
  onItemClick: (String) -> Unit,
  content: @Composable () -> Unit,
  modifier: Modifier = Modifier,
) {
  val items = listOf(
    MenuItem(
      id = NavDestination.HomeDestination.route,
      title = stringResource(R.string.home_menu_item),
      icon = Icons.Default.Home
    ),
    MenuItem(
      id = NavDestination.CategoriesDestination.route,
      title = stringResource(R.string.category_menu_item),
      icon = Icons.Default.Category
    ),
    MenuItem(
      id = NavDestination.FavoritesDestination.route,
      title = stringResource(R.string.favorite_menu_item),
      icon = Icons.Default.Favorite
    ),
    MenuItem(
      id = NavDestination.HistoryDestination.route,
      title = stringResource(R.string.history_menu_item),
      icon = Icons.Default.History
    ),
    MenuItem(
      id = NavDestination.ProfileDestination.route,
      title = stringResource(R.string.profile_menu_item),
      icon = Icons.Default.Person
    ),
    MenuItem(
      id = NavDestination.SettingsDestination.route,
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
            isUserLoggedIn = isUserLoggedIn,
            currentUser = currentUser,
            onSignInClick = onSignInClick,
            modifier = Modifier
              .weight(0.4f)
              .fillMaxWidth()
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