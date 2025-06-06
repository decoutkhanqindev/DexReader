package com.decoutkhanqindev.dexreader.presentation.ui.categories

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.navigation.NavigationDestination
import com.decoutkhanqindev.dexreader.presentation.ui.categories.components.CategoriesContent
import com.decoutkhanqindev.dexreader.presentation.ui.common.base.BaseScreen

@Composable
fun CategoriesScreen(
  onMenuItemClick: (String) -> Unit,
  onSearchClick: () -> Unit,
  onSelectedCategory: (String, String) -> Unit,
  viewModel: CategoriesViewModel = hiltViewModel(),
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val route by rememberSaveable {
    mutableStateOf(NavigationDestination.CategoriesDestination.route)
  }

  BaseScreen(
    title = stringResource(R.string.category_menu_item),
    route = route,
    onMenuItemClick = onMenuItemClick,
    onSearchClick = onSearchClick,
    content = {
      CategoriesContent(
        uiState = uiState,
        onSelectedCategory = onSelectedCategory,
        onRetry = { viewModel.retry() },
        modifier = Modifier.fillMaxSize()
      )
    },
    modifier = modifier
  )
}