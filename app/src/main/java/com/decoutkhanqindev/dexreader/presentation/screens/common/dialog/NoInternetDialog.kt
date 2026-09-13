package com.decoutkhanqindev.dexreader.presentation.screens.common.dialog

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun NoInternetDialog(modifier: Modifier = Modifier) {
  val context = LocalContext.current

  AlertDialog(
    icon = Icons.Default.WifiOff,
    title = stringResource(R.string.no_internet_connection),
    confirm = stringResource(R.string.open_settings),
    isEnableDismiss = false,
    modifier = modifier,
    onConfirmClick = {
      context.startActivity(
        Intent(
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) Settings.Panel.ACTION_INTERNET_CONNECTIVITY
          else Settings.ACTION_WIRELESS_SETTINGS
        )
      )
    },
  )
}

@Preview
@Composable
private fun NoInternetDialogPreview() {
  DexReaderTheme {
    NoInternetDialog()
  }
}
