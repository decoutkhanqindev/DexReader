package com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.SubmitButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun UpdateAndLogoutUserBottomBar(
  isShowUpdateButton: Boolean,
  modifier: Modifier = Modifier,
  onUpdateClick: () -> Unit,
  onLogoutClick: () -> Unit,
) {
  var isShowUpdateUserDialog by rememberSaveable { mutableStateOf(false) }
  var isShowLogoutUserDialog by rememberSaveable { mutableStateOf(false) }

  BottomAppBar(
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.surface,
    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
  ) {
    ActionButton(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth(),
      onClick = { isShowLogoutUserDialog = true }
    ) {
      Text(
        text = stringResource(R.string.logout),
        modifier = Modifier.fillMaxWidth(),
        color = Color.Red,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
      )
    }
    AnimatedVisibility(
      visible = isShowUpdateButton,
      enter = scaleIn(),
      exit = scaleOut(),
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
    ) {
      SubmitButton(
        title = stringResource(R.string.update_profile),
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 8.dp),
      ) { isShowUpdateUserDialog = true }
    }
  }

  if (isShowUpdateUserDialog) {
    NotificationDialog(
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
    NotificationDialog(
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