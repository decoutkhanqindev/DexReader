package com.decoutkhanqindev.dexreader.presentation.ui.profile.components

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
import com.decoutkhanqindev.dexreader.presentation.ui.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.ui.profile.ProfileUiState
import com.decoutkhanqindev.dexreader.presentation.ui.profile.components.actions.ProfileNameEdit
import com.decoutkhanqindev.dexreader.presentation.ui.profile.components.actions.ProfilePicturePicker

@Composable
fun ProfileContent(
  uiState: ProfileUiState,
  onUpdateNameChange: (String) -> Unit,
  onUpdatePicUrlChange: (String) -> Unit,
  onLogoutSuccess: () -> Unit,
  onRetryUpdate: () -> Unit,
  onRetryLogout: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isShowUpdateDialog by rememberSaveable { mutableStateOf(true) }
  var isShowLogoutDialog by rememberSaveable { mutableStateOf(true) }
  val currentUser = uiState.user

  Box(modifier = modifier) {
    Box(modifier = modifier) {
      when {
        uiState.isLoading -> LoadingScreen(modifier = Modifier.fillMaxSize())

        uiState.isUpdateUserError -> {
          if (isShowUpdateDialog) {
            NotificationDialog(
              title = stringResource(R.string.update_profile_failed),
              onDismissClick = { isShowUpdateDialog = false },
              onConfirmClick = onRetryUpdate,
            )
          }
        }

        uiState.isLogoutUserError -> {
          if (isShowLogoutDialog) {
            NotificationDialog(
              title = stringResource(R.string.logout_failed_please_try_again),
              onDismissClick = { isShowLogoutDialog = false },
              onConfirmClick = onRetryLogout,
            )
          }
        }

        uiState.isUpdateUserSuccess -> {
          if (isShowUpdateDialog) {
            NotificationDialog(
              icon = Icons.Default.Done,
              title = stringResource(R.string.your_profile_has_been_updated_successfully),
              confirm = stringResource(R.string.ok),
              onConfirmClick = { isShowUpdateDialog = false },
            )
          }
        }

        uiState.isLogoutUserSuccess -> {
          if (isShowLogoutDialog) {
            NotificationDialog(
              icon = Icons.Default.Done,
              title = stringResource(R.string.logout_successful),
              confirm = stringResource(R.string.ok),
              onConfirmClick = {
                isShowLogoutDialog = false
                onLogoutSuccess()
              },
            )
          }
        }
      }

      Box(
        modifier = if (uiState.isLoading) Modifier
          .fillMaxSize()
          .blur(8.dp)
        else Modifier.fillMaxSize()
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.spacedBy(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          ProfilePicturePicker(
            url = uiState.updatedProfilePictureUrl ?: currentUser?.profilePictureUrl,
            name = uiState.updatedName ?: currentUser?.name ?: "",
            onSelectedImageUrl = onUpdatePicUrlChange,
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 16.dp)
          )
          ProfileNameEdit(
            name = uiState.updatedName ?: currentUser?.name ?: "",
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
    }
  }
}