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
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.settings.SettingsUiState
import com.decoutkhanqindev.dexreader.presentation.model.value.settings.ThemeModeValue

@Composable
fun SettingsContent(
  uiState: SettingsUiState,
  onThemeOptionClick: (ThemeModeValue) -> Unit,
  onSaveThemeOption: () -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var isShowSaveDialog by rememberSaveable { mutableStateOf(false) }
  var isShowSuccessDialog by rememberSaveable { mutableStateOf(true) }
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }

  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier,
  ) {
    Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.fillMaxSize().let { if (uiState.isLoading) it.blur(8.dp) else it }
    ) {
      ThemeOptionList(
        selectedItem = uiState.themeOption,
        onItemClick = {
          isShowSaveDialog = true
          onThemeOptionClick(it)
        },
        modifier = Modifier
      )
    }

    when {
      uiState.isLoading -> LoadingScreen(modifier = Modifier.fillMaxSize())
      uiState.isError -> {
        if (isShowErrorDialog) {
          NotificationDialog(
            title = stringResource(R.string.change_theme_failed),
            onDismissClick = { isShowErrorDialog = false },
            onConfirmClick = {
              isShowErrorDialog = false
              onRetry()
            },
          )
        }
      }

      uiState.isSuccess -> {
        if (isShowSuccessDialog) {
          NotificationDialog(
            icon = Icons.Default.Done,
            title = stringResource(R.string.theme_change_successful),
            isEnableDismiss = false,
            confirm = stringResource(R.string.ok),
            onConfirmClick = { isShowSuccessDialog = false },
          )
        }
      }
    }

    if (isShowSaveDialog) {
      NotificationDialog(
        title = stringResource(R.string.are_you_sure_you_want_to_change_the_theme),
        onDismissClick = { isShowSaveDialog = false },
        confirm = stringResource(R.string.change),
        onConfirmClick = {
          isShowSaveDialog = false
          onSaveThemeOption()
        },
      )
    }
  }
}
