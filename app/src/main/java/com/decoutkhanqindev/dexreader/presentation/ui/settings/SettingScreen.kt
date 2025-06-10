package com.decoutkhanqindev.dexreader.presentation.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.presentation.navigation.NavDestination
import com.decoutkhanqindev.dexreader.presentation.ui.common.base.BaseScreen

@Composable
fun SettingsScreen(
  isUserLoggedIn: Boolean,
  currentUser: User?,
  onSignInClick: () -> Unit,
  onMenuItemClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val route = NavDestination.SettingsDestination.route

  BaseScreen(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    onSignInClick = onSignInClick,
    title = stringResource(R.string.settings_menu_item),
    route = route,
    onMenuItemClick = onMenuItemClick,
    isSearchEnabled = false,
    content = {},
    modifier = modifier
  )
}