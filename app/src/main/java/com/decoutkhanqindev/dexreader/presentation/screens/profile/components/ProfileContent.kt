package com.decoutkhanqindev.dexreader.presentation.screens.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.profile.ProfileUiState
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions.ProfileNameEdit
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions.ProfilePicturePicker

@Composable
fun ProfileContent(
  uiState: ProfileUiState,
  modifier: Modifier = Modifier,
  onUpdateNameChange: (String) -> Unit,
  onUpdatePicUrlChange: (String) -> Unit,
  onLogoutSuccess: () -> Unit,
  onRetryUpdate: () -> Unit,
  onRetryLogout: () -> Unit,
) {
  var isShowUpdateUserSuccessDialog by rememberSaveable { mutableStateOf(true) }
  var isShowUpdateUserErrorDialog by rememberSaveable { mutableStateOf(true) }
  var isShowLogoutUserSuccessDialog by rememberSaveable { mutableStateOf(true) }
  var isShowLogoutUserErrorDialog by rememberSaveable { mutableStateOf(true) }
  val currentUser = uiState.currentUser

  Box(
    modifier =
      if (uiState.isLoading) modifier.blur(8.dp)
      else modifier
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      ProfilePicturePicker(
        url = uiState.newAvatarUrl ?: currentUser?.avatarUrl,
        name = uiState.newName ?: currentUser?.name ?: "",
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp),
      ) { onUpdatePicUrlChange(it) }
      ProfileNameEdit(
        name = uiState.newName ?: currentUser?.name ?: "",
      ) { onUpdateNameChange(it) }
      Text(
        text = currentUser?.email ?: "",
        modifier = Modifier.fillMaxWidth(),
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Light,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge,
      )
    }
  }

  when {
    uiState.isLoading -> LoadingScreen(modifier = modifier)

    uiState.isUpdateUserError -> {
      if (isShowUpdateUserErrorDialog) {
        NotificationDialog(
          title = stringResource(R.string.update_profile_failed),
          onConfirmClick = {
            isShowUpdateUserErrorDialog = false
            onRetryUpdate()
          },
          onDismissClick = { isShowUpdateUserErrorDialog = false },
        )
      }
    }

    uiState.isLogoutUserError -> {
      if (isShowLogoutUserErrorDialog) {
        NotificationDialog(
          title = stringResource(R.string.logout_failed_please_try_again),
          onConfirmClick = {
            isShowLogoutUserErrorDialog = false
            onRetryLogout()
          },
          onDismissClick = { isShowLogoutUserErrorDialog = false },
        )
      }
    }

    uiState.isUpdateUserSuccess -> {
      if (isShowUpdateUserSuccessDialog) {
        NotificationDialog(
          icon = Icons.Default.Done,
          title = stringResource(R.string.your_profile_has_been_updated_successfully),
          confirm = stringResource(R.string.ok),
          isEnableDismiss = false,
          onConfirmClick = { isShowUpdateUserSuccessDialog = false },
        )
      }
    }

    uiState.isLogoutUserSuccess -> {
      if (isShowLogoutUserSuccessDialog) {
        NotificationDialog(
          icon = Icons.Default.Done,
          title = stringResource(R.string.logout_successful),
          confirm = stringResource(R.string.ok),
          isEnableDismiss = false,
          onConfirmClick = {
            isShowLogoutUserSuccessDialog = false
            onLogoutSuccess()
          },
        )
      }
    }
  }
}

private val previewUser = UserModel(
  id = "u-001",
  name = "Nguyen Van A",
  email = "nguyenvana@email.com",
  avatarUrl = null,
)

@Preview
@Composable
private fun ProfileContentIdlePreview() {
  ProfileContent(
    uiState = ProfileUiState(currentUser = previewUser),
    modifier = Modifier.fillMaxSize(),
    onUpdateNameChange = {},
    onUpdatePicUrlChange = {},
    onLogoutSuccess = {},
    onRetryUpdate = {},
    onRetryLogout = {}
  )
}

@Preview
@Composable
private fun ProfileContentLoadingPreview() {
  ProfileContent(
    uiState = ProfileUiState(currentUser = previewUser, isLoading = true),
    modifier = Modifier.fillMaxSize(),
    onUpdateNameChange = {},
    onUpdatePicUrlChange = {},
    onLogoutSuccess = {},
    onRetryUpdate = {},
    onRetryLogout = {}
  )
}

@Preview
@Composable
private fun ProfileContentUpdateSuccessPreview() {
  ProfileContent(
    uiState = ProfileUiState(currentUser = previewUser, isUpdateUserSuccess = true),
    modifier = Modifier.fillMaxSize(),
    onUpdateNameChange = {},
    onUpdatePicUrlChange = {},
    onLogoutSuccess = {},
    onRetryUpdate = {},
    onRetryLogout = {}
  )
}

@Preview
@Composable
private fun ProfileContentUpdateErrorPreview() {
  ProfileContent(
    uiState = ProfileUiState(currentUser = previewUser, isUpdateUserError = true),
    modifier = Modifier.fillMaxSize(),
    onUpdateNameChange = {},
    onUpdatePicUrlChange = {},
    onLogoutSuccess = {},
    onRetryUpdate = {},
    onRetryLogout = {}
  )
}

@Preview
@Composable
private fun ProfileContentLogoutErrorPreview() {
  ProfileContent(
    uiState = ProfileUiState(currentUser = previewUser, isLogoutUserError = true),
    modifier = Modifier.fillMaxSize(),
    onUpdateNameChange = {},
    onUpdatePicUrlChange = {},
    onLogoutSuccess = {},
    onRetryUpdate = {},
    onRetryLogout = {}
  )
}
