package com.decoutkhanqindev.dexreader.presentation.screens.profile.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.screens.profile.ProfileUiState
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions.ProfileNameEdit
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions.ProfilePicturePicker
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions.UpdateProfileButton
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun ProfileEditSection(
  uiState: ProfileUiState,
  isShowUpdateButton: Boolean,
  modifier: Modifier = Modifier,
  onUpdateNameChange: (String) -> Unit,
  onUpdatePicUrlChange: (String) -> Unit,
  onUpdateClick: () -> Unit,
) {
  val currentUser = uiState.currentUser

  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Top,
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      ProfilePicturePicker(
        url = uiState.newAvatarUrl ?: currentUser?.avatarUrl,
      ) { onUpdatePicUrlChange(it) }

      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.Center
      ) {
        ProfileNameEdit(
          name = uiState.newName ?: currentUser?.name ?: "",
        ) { onUpdateNameChange(it) }

        Text(
          text = currentUser?.email ?: "",
          modifier = Modifier.fillMaxWidth(),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Medium,
          style = MaterialTheme.typography.bodyLarge,
        )
      }
    }

    UpdateProfileButton(
      isVisible = isShowUpdateButton,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp),
      onUpdateClick = onUpdateClick,
    )
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
private fun ProfileEditSectionPreview() {
  DexReaderTheme {
    ProfileEditSection(
      uiState = ProfileUiState(currentUser = previewUser),
      isShowUpdateButton = false,
      modifier = Modifier.fillMaxWidth(),
      onUpdateNameChange = {},
      onUpdatePicUrlChange = {},
      onUpdateClick = {},
    )
  }
}

@Preview
@Composable
private fun ProfileEditSectionWithUpdateButtonPreview() {
  DexReaderTheme {
    ProfileEditSection(
      uiState = ProfileUiState(currentUser = previewUser, newName = "New Name"),
      isShowUpdateButton = true,
      modifier = Modifier.fillMaxWidth(),
      onUpdateNameChange = {},
      onUpdatePicUrlChange = {},
      onUpdateClick = {},
    )
  }
}
