package com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.SubmitButton
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

  Surface(
    modifier = modifier,
    tonalElevation = 8.dp,
    shadowElevation = 16.dp,
    color = MaterialTheme.colorScheme.surface,
    shape = MaterialTheme.shapes.extraLarge // Match modern theme
  ) {
    Row(
      modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      TextButton(
        onClick = { isShowLogoutUserDialog = true },
        modifier = Modifier.weight(1f),
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
      ) {
        Text(
          text = stringResource(R.string.logout),
          fontWeight = FontWeight.Bold,
          style = MaterialTheme.typography.titleMedium
        )
      }

      AnimatedVisibility(
        visible = isShowUpdateButton,
        enter = scaleIn(),
        exit = scaleOut(),
        modifier = Modifier.weight(1.5f)
      ) {
        SubmitButton(
          title = stringResource(R.string.update_profile),
          modifier = Modifier.fillMaxWidth(),
          onClick = { isShowUpdateUserDialog = true }
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