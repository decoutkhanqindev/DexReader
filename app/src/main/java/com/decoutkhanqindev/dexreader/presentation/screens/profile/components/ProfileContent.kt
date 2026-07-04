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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.profile.ProfileUiState
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions.ProfileNameEdit
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions.ProfilePicturePicker
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface

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
  var isShowUpdateUserSuccessDialog by remember { mutableStateOf(false) } // Fixed initial state
  var isShowUpdateUserErrorDialog by remember { mutableStateOf(false) } // Fixed initial state
  var isShowLogoutUserSuccessDialog by remember { mutableStateOf(false) } // Fixed initial state
  var isShowLogoutUserErrorDialog by remember { mutableStateOf(false) } // Fixed initial state
  val currentUser = uiState.currentUser

  // ... (LaunchedEffects remain same)

  Box(
    modifier = if (uiState.isLoading) modifier.blur(8.dp)
    else modifier
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(24.dp),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Surface(
        modifier = Modifier.size(140.dp),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
      ) {
        ProfilePicturePicker(
          url = uiState.newAvatarUrl ?: currentUser?.avatarUrl,
          name = uiState.newName ?: currentUser?.name ?: "",
          modifier = Modifier.fillMaxSize(),
        ) { onUpdatePicUrlChange(it) }
      }

      Spacer(modifier = Modifier.height(24.dp))

      ProfileNameEdit(
        name = uiState.newName ?: currentUser?.name ?: "",
      ) { onUpdateNameChange(it) }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = currentUser?.email ?: "",
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Medium,
        style = MaterialTheme.typography.bodyLarge,
      )

      Spacer(modifier = Modifier.height(32.dp))
      
      HorizontalDivider(
          modifier = Modifier.fillMaxWidth(),
          thickness = 1.dp,
          color = MaterialTheme.colorScheme.outlineVariant
      )
      
      // We could add more profile options here (like Reading History link, Settings, etc.)
    }
  }

  when {
    uiState.isLoading -> LoadingScreen(modifier = modifier)

    uiState.isUpdateUserError -> {
      if (isShowUpdateUserErrorDialog) {
        AlertDialog(
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
        AlertDialog(
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
        AlertDialog(
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
        AlertDialog(
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
  DexReaderTheme {
    ProfileContent(
      uiState = ProfileUiState(currentUser = previewUser),
      modifier = Modifier.fillMaxSize(),
      onUpdateNameChange = {},
      onUpdatePicUrlChange = {},
      onLogoutSuccess = {},
      onRetryUpdate = {},
      onRetryLogout = {})
  }
}

@Preview
@Composable
private fun ProfileContentLoadingPreview() {
  DexReaderTheme {
    ProfileContent(
      uiState = ProfileUiState(currentUser = previewUser, isLoading = true),
      modifier = Modifier.fillMaxSize(),
      onUpdateNameChange = {},
      onUpdatePicUrlChange = {},
      onLogoutSuccess = {},
      onRetryUpdate = {},
      onRetryLogout = {})
  }
}

@Preview
@Composable
private fun ProfileContentUpdateSuccessPreview() {
  DexReaderTheme {
    ProfileContent(
      uiState = ProfileUiState(currentUser = previewUser, isUpdateUserSuccess = true),
      modifier = Modifier.fillMaxSize(),
      onUpdateNameChange = {},
      onUpdatePicUrlChange = {},
      onLogoutSuccess = {},
      onRetryUpdate = {},
      onRetryLogout = {})
  }
}

@Preview
@Composable
private fun ProfileContentUpdateErrorPreview() {
  DexReaderTheme {
    ProfileContent(
      uiState = ProfileUiState(currentUser = previewUser, isUpdateUserError = true),
      modifier = Modifier.fillMaxSize(),
      onUpdateNameChange = {},
      onUpdatePicUrlChange = {},
      onLogoutSuccess = {},
      onRetryUpdate = {},
      onRetryLogout = {})
  }
}

@Preview
@Composable
private fun ProfileContentLogoutErrorPreview() {
  DexReaderTheme {
    ProfileContent(
      uiState = ProfileUiState(currentUser = previewUser, isLogoutUserError = true),
      modifier = Modifier.fillMaxSize(),
      onUpdateNameChange = {},
      onUpdatePicUrlChange = {},
      onLogoutSuccess = {},
      onRetryUpdate = {},
      onRetryLogout = {})
  }
}
