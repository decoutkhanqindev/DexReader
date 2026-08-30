package com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun LogoutButton(
  modifier: Modifier = Modifier,
  onLogoutClick: () -> Unit,
) {
  var isShowLogoutUserDialog by remember { mutableStateOf(false) }

  ActionButton(
    backgroundColor = MaterialTheme.colorScheme.errorContainer,
    modifier = modifier,
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
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onErrorContainer,
      modifier = Modifier
        .size(24.dp)
        .padding(start = 8.dp)
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
private fun LogoutButtonPreview() {
  DexReaderTheme {
    LogoutButton(
      modifier = Modifier.fillMaxWidth(),
      onLogoutClick = {}
    )
  }
}
