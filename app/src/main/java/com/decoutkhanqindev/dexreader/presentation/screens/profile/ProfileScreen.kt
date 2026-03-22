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
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.ProfileContent
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions.UpdateAndLogoutUserBottomBar

@Composable
fun ProfileScreen(
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  onSignInClick: () -> Unit,
  onMenuItemClick: (MenuItemValue) -> Unit,
  onLogoutSuccess: () -> Unit,
  viewModel: ProfileViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
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
    onNavigateToSignInScreen = onSignInClick,
    selectedMenuItem = MenuItemValue.PROFILE,
    onNavigateToMenuItemScreen = onMenuItemClick,
    isSearchEnabled = false,
    bottomBar = {
      if (isUserLoggedIn) {
        UpdateAndLogoutUserBottomBar(
          isShowUpdateButton = isShowUpdateButton,
          onUpdateClick = viewModel::updateUserProfile,
          onLogoutClick = viewModel::logoutUser,
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    content = {
      if (isUserLoggedIn || uiState.isLogoutUserSuccess) {
        ProfileContent(
          uiState = uiState,
          onUpdateNameChange = viewModel::updateUserName,
          onUpdatePicUrlChange = viewModel::updateUserPicUrl,
          onLogoutSuccess = onLogoutSuccess,
          onRetryUpdate = viewModel::retryUpdateUserProfile,
          onRetryLogout = viewModel::retryLogoutUser,
          modifier = Modifier.fillMaxSize()
        )
      } else {
        IdleScreen(
          message = stringResource(R.string.please_sign_in_to_view_your_profile),
          modifier = Modifier.fillMaxSize()
        )
      }
    },
    modifier = modifier
  )
}