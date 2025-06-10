package com.decoutkhanqindev.dexreader.presentation.ui.history

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.presentation.navigation.NavDestination
import com.decoutkhanqindev.dexreader.presentation.ui.common.base.BaseScreen

@Composable
fun HistoryScreen(
  isUserLoggedIn: Boolean,
  currentUser: User?,
  onSignInClick: () -> Unit,
  onMenuItemClick: (String) -> Unit,
  onSearchClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val route = NavDestination.HistoryDestination.route

  BaseScreen(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    onSignInClick = onSignInClick,
    title = stringResource(R.string.history_menu_item),
    route = route,
    onMenuItemClick = onMenuItemClick,
    onSearchClick = onSearchClick,
    content = {},
    modifier = modifier
  )
}