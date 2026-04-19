package com.decoutkhanqindev.dexreader.presentation.screens.common.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDialog(
  icon: ImageVector = Icons.Default.ErrorOutline,
  title: String = stringResource(R.string.oops_something_went_wrong_please_try_again),
  confirm: String = stringResource(R.string.ok),
  dismiss: String = stringResource(R.string.cancel),
  isEnableDismiss: Boolean = true,
  modifier: Modifier = Modifier,
  onConfirmClick: () -> Unit,
  onDismissClick: () -> Unit = {},
  onDismissOuterClick: () -> Unit = {},
) {
  AlertDialog(
    onDismissRequest = onDismissOuterClick,
    confirmButton = {
      TextButton(onClick = onConfirmClick) {
        Text(
          text = confirm,
          fontWeight = FontWeight.Bold,
          style = MaterialTheme.typography.titleMedium,
        )
      }
    },
    modifier = modifier,
    dismissButton = {
      if (isEnableDismiss) {
        TextButton(onClick = onDismissClick) {
          Text(
            text = dismiss,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
          )
        }
      }
    },
    title = {
      Icon(
        imageVector = icon,
        contentDescription = title,
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
      )
    },
    text = {
      Text(
        text = title,
        modifier = Modifier.fillMaxWidth(),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleMedium,
      )
    },
    shape = MaterialTheme.shapes.large,
  )
}

@Preview
@Composable
private fun NotificationDialogPreview() {
  DexReaderTheme {
    NotificationDialog(onConfirmClick = {})
  }
}

@Preview
@Composable
private fun NotificationDialogNoDismissPreview() {
  DexReaderTheme {
    NotificationDialog(
      isEnableDismiss = false,
      onConfirmClick = {}
    )
  }
}

@Preview
@Composable
private fun NotificationDialogCustomPreview() {
  DexReaderTheme {
    NotificationDialog(
      title = "Update available. Please restart the app to apply changes.",
      confirm = "Restart",
      onConfirmClick = {}
    )
  }
}