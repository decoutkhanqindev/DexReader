package com.decoutkhanqindev.dexreader.presentation.screens.profile


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.ProfileContent
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions.UpdateAndLogoutUserBottomBar

@Composable
fun ProfileScreen(
  viewModel: ProfileViewModel = hiltViewModel(),
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  modifier: Modifier = Modifier,
  onNavigateToLoginScreen: () -> Unit,
  onNavigateToMenuItemScreen: (MenuValue) -> Unit,
  onNavigateToHomeScreen: () -> Unit,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val isShowUpdateButton by remember(uiState) {
    derivedStateOf {
      val nameChanged = uiState.newName != null
          && uiState.newName != currentUser?.name
      val picChanged = uiState.newAvatarUrl != null
          && uiState.newAvatarUrl != currentUser?.avatarUrl
      nameChanged || picChanged
    }
  }

  LaunchedEffect(isUserLoggedIn, currentUser) {
    if (isUserLoggedIn && currentUser != null) viewModel.updateCurrentUser(value = currentUser)
    else viewModel.updateCurrentUser(value = null)
  }

  BaseScreen(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    selectedMenuItem = MenuValue.PROFILE,
    isSearchEnabled = false,
    modifier = modifier,
    onNavigateToSignInScreen = onNavigateToLoginScreen,
    onNavigateToMenuItemScreen = onNavigateToMenuItemScreen,
    bottomBar = {
      if (isUserLoggedIn) {
        UpdateAndLogoutUserBottomBar(
          isShowUpdateButton = isShowUpdateButton,
          modifier = Modifier.fillMaxWidth(),
          onUpdateClick = viewModel::updateUserProfile,
          onLogoutClick = viewModel::logoutUser,
        )
      }
    }
  ) {
    if (isUserLoggedIn || uiState.isLogoutUserSuccess) {
      ProfileContent(
        uiState = uiState,
        modifier = Modifier.fillMaxSize(),
        onUpdateNameChange = viewModel::updateUserName,
        onUpdatePicUrlChange = viewModel::updateUserPicUrl,
        onLogoutSuccess = onNavigateToHomeScreen,
        onRetryUpdate = viewModel::retryUpdateUserProfile,
        onRetryLogout = viewModel::retryLogoutUser,
      )
    } else {
      IdleScreen(
        message = stringResource(R.string.please_sign_in_to_view_your_profile),
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}