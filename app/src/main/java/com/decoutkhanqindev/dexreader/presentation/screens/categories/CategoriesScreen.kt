package com.decoutkhanqindev.dexreader.presentation.screens.categories

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.screens.categories.components.CategoriesContent
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue

@Composable
fun CategoriesScreen(
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  onSignInClick: () -> Unit,
  onMenuItemClick: (MenuItemValue) -> Unit,
  onSearchClick: () -> Unit,
  onCategoryClick: (String, String) -> Unit,
  viewModel: CategoriesViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  BaseScreen(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    onSignInClick = onSignInClick,
    selectedMenuItem = MenuItemValue.CATEGORIES,
    onMenuItemClick = onMenuItemClick,
    onSearchClick = onSearchClick,
    content = {
      CategoriesContent(
        uiState = uiState,
        onCategoryClick = onCategoryClick,
        onRetry = viewModel::retry,
        modifier = Modifier.fillMaxSize()
      )
    },
    modifier = modifier
  )
}