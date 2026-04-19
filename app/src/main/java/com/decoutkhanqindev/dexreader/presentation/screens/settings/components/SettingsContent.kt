package com.decoutkhanqindev.dexreader.presentation.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.value.settings.ThemeModeValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.settings.SettingsUiState
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun SettingsContent(
  uiState: SettingsUiState,
  modifier: Modifier = Modifier,
  onThemeOptionClick: (ThemeModeValue) -> Unit,
  onSaveThemeOption: () -> Unit,
  onRetry: () -> Unit,
) {
  var isShowSaveDialog by rememberSaveable { mutableStateOf(false) }
  var isShowSuccessDialog by rememberSaveable { mutableStateOf(true) }
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }

  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
  ) {
    Column(
      modifier = Modifier.fillMaxSize().let { if (uiState.isLoading) it.blur(8.dp) else it },
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      ThemeOptionList(
        selectedItem = uiState.themeOption,
        modifier = Modifier,
      ) {
        isShowSaveDialog = true
        onThemeOptionClick(it)
      }
    }

    when {
      uiState.isLoading -> LoadingScreen(modifier = Modifier.fillMaxSize())
      uiState.isError -> {
        if (isShowErrorDialog) {
          NotificationDialog(
            title = stringResource(R.string.change_theme_failed),
            onConfirmClick = {
              isShowErrorDialog = false
              onRetry()
            },
            onDismissClick = { isShowErrorDialog = false },
          )
        }
      }

      uiState.isSuccess -> {
        if (isShowSuccessDialog) {
          NotificationDialog(
            icon = Icons.Default.Done,
            title = stringResource(R.string.theme_change_successful),
            confirm = stringResource(R.string.ok),
            isEnableDismiss = false,
            onConfirmClick = { isShowSuccessDialog = false },
          )
        }
      }
    }

    if (isShowSaveDialog) {
      NotificationDialog(
        title = stringResource(R.string.are_you_sure_you_want_to_change_the_theme),
        confirm = stringResource(R.string.change),
        onConfirmClick = {
          isShowSaveDialog = false
          onSaveThemeOption()
        },
        onDismissClick = { isShowSaveDialog = false },
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun SettingsContentDefaultPreview() {
  DexReaderTheme {
    SettingsContent(
      uiState = SettingsUiState(themeOption = ThemeModeValue.SYSTEM),
      modifier = Modifier.fillMaxSize(),
      onThemeOptionClick = {},
      onSaveThemeOption = {},
      onRetry = {},
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun SettingsContentLoadingPreview() {
  DexReaderTheme {
    SettingsContent(
      uiState = SettingsUiState(isLoading = true, themeOption = ThemeModeValue.DARK),
      modifier = Modifier.fillMaxSize(),
      onThemeOptionClick = {},
      onSaveThemeOption = {},
      onRetry = {},
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun SettingsContentSuccessPreview() {
  DexReaderTheme {
    SettingsContent(
      uiState = SettingsUiState(isSuccess = true, themeOption = ThemeModeValue.LIGHT),
      modifier = Modifier.fillMaxSize(),
      onThemeOptionClick = {},
      onSaveThemeOption = {},
      onRetry = {},
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun SettingsContentErrorPreview() {
  DexReaderTheme {
    SettingsContent(
      uiState = SettingsUiState(isError = true, themeOption = ThemeModeValue.SYSTEM),
      modifier = Modifier.fillMaxSize(),
      onThemeOptionClick = {},
      onSaveThemeOption = {},
      onRetry = {},
    )
  }
}
