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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.presentation.navigation.NavDestination
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.ProfileContent
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions.UpdateAndLogoutUserBottomBar

@Composable
fun ProfileScreen(
  isUserLoggedIn: Boolean,
  currentUser: User?,
  onSignInClick: () -> Unit,
  onMenuItemClick: (String) -> Unit,
  onLogoutSuccess: () -> Unit,
  viewModel: ProfileViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val route = NavDestination.ProfileDestination.route
  val isShowUpdateButton by remember(uiState) {
    derivedStateOf {
      val nameChanged = uiState.updatedName != null
          && uiState.updatedName != currentUser?.name
      val picChanged = uiState.updatedProfilePictureUrl != null
          && uiState.updatedProfilePictureUrl != currentUser?.profilePictureUrl
      nameChanged || picChanged
    }
  }

  LaunchedEffect(isUserLoggedIn, currentUser) {
    if (isUserLoggedIn && currentUser != null) viewModel.updateCurrentUser(user = currentUser)
    else viewModel.updateCurrentUser(user = null)
  }

  BaseScreen(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    onSignInClick = onSignInClick,
    title = stringResource(R.string.profile_menu_item),
    route = route,
    onMenuItemClick = onMenuItemClick,
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