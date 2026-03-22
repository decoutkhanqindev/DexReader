package com.decoutkhanqindev.dexreader.presentation.screens.categories

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue
import com.decoutkhanqindev.dexreader.presentation.screens.categories.components.CategoriesContent
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen

@Composable
fun CategoriesScreen(
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  onNavigateToLoginScreen: () -> Unit,
  onNavigateToMenuItemScreen: (MenuItemValue) -> Unit,
  onNavigateToSearchScreen: () -> Unit,
  onNavigateCategoryDetailScreen: (String, String) -> Unit,
  viewModel: CategoriesViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  BaseScreen(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    onNavigateToSignInScreen = onNavigateToLoginScreen,
    selectedMenuItem = MenuItemValue.CATEGORIES,
    onNavigateToMenuItemScreen = onNavigateToMenuItemScreen,
    onNavigateToSearchScreen = onNavigateToSearchScreen,
    content = {
      CategoriesContent(
        uiState = uiState,
        onCategoryClick = onNavigateCategoryDetailScreen,
        onRetry = viewModel::retry,
        modifier = Modifier.fillMaxSize()
      )
    },
    modifier = modifier
  )
}