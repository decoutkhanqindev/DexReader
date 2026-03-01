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
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.profile.ProfileUiState
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions.ProfileNameEdit
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions.ProfilePicturePicker

@Composable
fun ProfileContent(
  uiState: ProfileUiState,
  onUpdateNameChange: (String) -> Unit,
  onUpdatePicUrlChange: (String) -> Unit,
  onLogoutSuccess: () -> Unit,
  onRetryUpdate: () -> Unit,
  onRetryLogout: () -> Unit,
  modifier: Modifier = Modifier,
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
        onSelectedImageUrl = onUpdatePicUrlChange,
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp)
      )
      ProfileNameEdit(
        name = uiState.newName ?: currentUser?.name ?: "",
        onNameChange = onUpdateNameChange,
      )
      Text(
        text = currentUser?.email ?: "",
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }

  when {
    uiState.isLoading -> LoadingScreen(modifier = modifier)

    uiState.isUpdateUserError -> {
      if (isShowUpdateUserErrorDialog) {
        NotificationDialog(
          title = stringResource(R.string.update_profile_failed),
          onDismissClick = { isShowUpdateUserErrorDialog = false },
          onConfirmClick = {
            isShowUpdateUserErrorDialog = false
            onRetryUpdate()
          },
        )
      }
    }

    uiState.isLogoutUserError -> {
      if (isShowLogoutUserErrorDialog) {
        NotificationDialog(
          title = stringResource(R.string.logout_failed_please_try_again),
          onDismissClick = { isShowLogoutUserErrorDialog = false },
          onConfirmClick = {
            isShowLogoutUserErrorDialog = false
            onRetryLogout()
          },
        )
      }
    }

    uiState.isUpdateUserSuccess -> {
      if (isShowUpdateUserSuccessDialog) {
        NotificationDialog(
          icon = Icons.Default.Done,
          title = stringResource(R.string.your_profile_has_been_updated_successfully),
          isEnableDismiss = false,
          confirm = stringResource(R.string.ok),
          onConfirmClick = { isShowUpdateUserSuccessDialog = false },
        )
      }
    }

    uiState.isLogoutUserSuccess -> {
      if (isShowLogoutUserSuccessDialog) {
        NotificationDialog(
          icon = Icons.Default.Done,
          title = stringResource(R.string.logout_successful),
          isEnableDismiss = false,
          confirm = stringResource(R.string.ok),
          onConfirmClick = {
            isShowLogoutUserSuccessDialog = false
            onLogoutSuccess()
          },
        )
      }
    }
  }
}
