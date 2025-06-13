package com.decoutkhanqindev.dexreader.presentation.ui.profile.components.actions

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
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.ui.common.buttons.SubmitButton
import com.decoutkhanqindev.dexreader.presentation.ui.common.dialog.NotificationDialog

@Composable
fun UpdateAndLogoutUserBottomBar(
  isShowUpdateButton: Boolean,
  onUpdateClick: () -> Unit,
  onLogoutClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isShowUpdateDialog by rememberSaveable { mutableStateOf(false) }
  var isShowLogoutDialog by rememberSaveable { mutableStateOf(false) }

  if (isShowUpdateDialog) {
    NotificationDialog(
      title = stringResource(R.string.are_you_sure_you_want_to_update_with_the_new_changes),
      onDismissClick = { isShowUpdateDialog = false },
      confirm = stringResource(R.string.update_profile),
      onConfirmClick = {
        onUpdateClick()
        isShowUpdateDialog = false
      },
    )
  }

  if (isShowLogoutDialog) {
    NotificationDialog(
      title = stringResource(R.string.are_you_sure_you_want_to_logout),
      onDismissClick = { isShowLogoutDialog = false },
      confirm = stringResource(R.string.logout),
      onConfirmClick = {
        onLogoutClick()
        isShowLogoutDialog = false
      },
    )
  }

  BottomAppBar(
    actions = {
      ActionButton(
        onClick = { isShowLogoutDialog = true },
        content = {
          Text(
            text = stringResource(R.string.logout),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            color = Color.Red,
            modifier = Modifier.fillMaxWidth()
          )
        },
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
      )
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
          onSubmitClick = { isShowUpdateDialog = true },
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
        )
      }
    },
    containerColor = MaterialTheme.colorScheme.surface,
    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    modifier = modifier
  )
}