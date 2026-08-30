package com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun UpdateProfileButton(
  isVisible: Boolean,
  modifier: Modifier = Modifier,
  onUpdateClick: () -> Unit,
) {
  var isShowUpdateUserDialog by remember { mutableStateOf(false) }

  AnimatedVisibility(
    visible = isVisible,
    enter = scaleIn(),
    exit = scaleOut(),
    modifier = modifier
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
}

@Preview
@Composable
private fun UpdateProfileButtonVisiblePreview() {
  DexReaderTheme {
    UpdateProfileButton(
      isVisible = true,
      modifier = Modifier.fillMaxWidth(),
      onUpdateClick = {}
    )
  }
}
