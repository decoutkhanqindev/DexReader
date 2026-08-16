package com.decoutkhanqindev.dexreader.presentation.screens.categories

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue
import com.decoutkhanqindev.dexreader.presentation.screens.categories.components.CategoriesContent
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen

@Composable
fun CategoriesScreen(
  viewModel: CategoriesViewModel = hiltViewModel(),
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  modifier: Modifier = Modifier,
  onNavigateToLoginScreen: () -> Unit,
  onNavigateToMenuItemScreen: (MenuValue) -> Unit,
  onNavigateToSearchScreen: () -> Unit,
  onNavigateToCategoryScreen: (categoryId: String, title: String) -> Unit,
) {
  val categoryListUiState by viewModel.categoryListUiState.collectAsStateWithLifecycle()
  val categoryCoverStates by viewModel.categoryCoverStates.collectAsStateWithLifecycle()

  BaseScreen(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    selectedMenuItem = MenuValue.CATEGORIES,
    modifier = modifier,
    onNavigateToSignInScreen = onNavigateToLoginScreen,
    onNavigateToMenuItemScreen = onNavigateToMenuItemScreen,
    onNavigateToSearchScreen = onNavigateToSearchScreen
  ) {
    CategoriesContent(
      categoryListUiState = categoryListUiState,
      categoryCoverUiState = categoryCoverStates,
      modifier = Modifier.fillMaxSize(),
      onCategoryClick = onNavigateToCategoryScreen,
      onLoadCover = { viewModel.loadCategoryCover(it) },
      onRetry = { viewModel.retry() },
    )
  }
}