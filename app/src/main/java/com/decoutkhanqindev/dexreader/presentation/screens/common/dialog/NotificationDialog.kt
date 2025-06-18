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
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDialog(
  icon: ImageVector = Icons.Default.ErrorOutline,
  title: String = stringResource(R.string.oops_something_went_wrong_please_try_again),
  dismiss: String = stringResource(R.string.cancel),
  isEnableDismiss: Boolean = true,
  onDismissClick: () -> Unit = {},
  confirm: String = stringResource(R.string.retry),
  onConfirmClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  AlertDialog(
    modifier = modifier,
    shape = MaterialTheme.shapes.large,
    onDismissRequest = onDismissClick,
    confirmButton = {
      TextButton(onClick = onConfirmClick) {
        Text(
          text = confirm,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
        )
      }
    },
    dismissButton = {
      if (isEnableDismiss) {
        TextButton(onClick = onDismissClick) {
          Text(
            text = dismiss,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
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
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
      )
    }
  )
}