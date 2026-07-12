package com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun UpdateAndLogoutUserBottomBar(
  isShowUpdateButton: Boolean,
  modifier: Modifier = Modifier,
  onUpdateClick: () -> Unit,
  onLogoutClick: () -> Unit,
) {
  var isShowUpdateUserDialog by remember { mutableStateOf(false) }
  var isShowLogoutUserDialog by remember { mutableStateOf(false) }

  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    ActionButton(
      backgroundColor = MaterialTheme.colorScheme.errorContainer,
      modifier = Modifier.weight(1f),
      onClick = { isShowLogoutUserDialog = true }
    ) {
      Text(
        text = stringResource(R.string.logout),
        color = MaterialTheme.colorScheme.onErrorContainer,
        fontWeight = FontWeight.ExtraBold,
        style = MaterialTheme.typography.titleMedium,
      )

      Icon(
        imageVector = Icons.AutoMirrored.Filled.Logout,
        contentDescription = stringResource(R.string.logout),
        tint = MaterialTheme.colorScheme.onErrorContainer,
        modifier = Modifier
          .size(24.dp)
          .padding(start = 8.dp)
      )
    }

    AnimatedVisibility(
      visible = isShowUpdateButton,
      enter = scaleIn(),
      exit = scaleOut(),
      modifier = Modifier.weight(1f)
    ) {
      ActionButton(
        backgroundColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.fillMaxWidth(),
        onClick = { isShowUpdateUserDialog = true }
      ) {
        Text(
          text = stringResource(R.string.update_profile),
          color = MaterialTheme.colorScheme.onPrimary,
          fontWeight = FontWeight.ExtraBold,
          style = MaterialTheme.typography.titleMedium,
        )
      }
    }
  }

  if (isShowUpdateUserDialog) {
    AlertDialog(
      title = stringResource(R.string.are_you_sure_you_want_to_update_with_the_new_changes),
      confirm = stringResource(R.string.update_profile),
      onConfirmClick = {
        onUpdateClick()
        isShowUpdateUserDialog = false
      },
      onDismissClick = { isShowUpdateUserDialog = false },
    )
  }

  if (isShowLogoutUserDialog) {
    AlertDialog(
      title = stringResource(R.string.are_you_sure_you_want_to_logout),
      confirm = stringResource(R.string.logout),
      onConfirmClick = {
        onLogoutClick()
        isShowLogoutUserDialog = false
      },
      onDismissClick = { isShowLogoutUserDialog = false },
    )
  }
}

@Preview
@Composable
private fun UpdateAndLogoutUserBottomBarWithUpdatePreview() {
  DexReaderTheme {
    UpdateAndLogoutUserBottomBar(
      isShowUpdateButton = true,
      onUpdateClick = {},
      onLogoutClick = {}
    )
  }
}

@Preview
@Composable
private fun UpdateAndLogoutUserBottomBarLogoutOnlyPreview() {
  DexReaderTheme {
    UpdateAndLogoutUserBottomBar(
      isShowUpdateButton = false,
      onUpdateClick = {},
      onLogoutClick = {}
    )
  }
}