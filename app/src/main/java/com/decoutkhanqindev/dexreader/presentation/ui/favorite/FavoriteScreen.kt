package com.decoutkhanqindev.dexreader.presentation.ui.favorite

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.navigation.NavDestination
import com.decoutkhanqindev.dexreader.presentation.ui.common.base.BaseScreen

@Composable
fun FavoriteScreen(
  onMenuItemClick: (String) -> Unit,
  onSearchClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val route = NavDestination.FavoriteDestination.route
  
  BaseScreen(
    title = stringResource(R.string.favorite_menu_item),
    route = route,
    onMenuItemClick = onMenuItemClick,
    onSearchClick = onSearchClick,
    content = {},
    modifier = modifier
  )
}