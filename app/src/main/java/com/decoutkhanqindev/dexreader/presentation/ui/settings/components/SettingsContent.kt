package com.decoutkhanqindev.dexreader.presentation.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.ThemeType
import com.decoutkhanqindev.dexreader.presentation.ui.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.ui.settings.SettingsUiState

@Composable
fun SettingsContent(
  uiState: SettingsUiState,
  onChangeThemeType: (ThemeType) -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedThemeType by rememberSaveable { mutableStateOf(uiState.themeType) }
  var isShowChangeThemeErrorDialog by rememberSaveable { mutableStateOf(true) }
  var isShowChangeThemeSuccessDialog by rememberSaveable { mutableStateOf(true) }
  var isShowConfirmChangeThemeDialog by rememberSaveable { mutableStateOf(false) }

  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
  ) {
    ThemeSelectorList(
      selectedThemeType = selectedThemeType,
      onSelectedTheme = {
        isShowConfirmChangeThemeDialog = true
        selectedThemeType = it
      },
      modifier = Modifier.size(200.dp)
    )
  }

  when {
    uiState.isLoading -> LoadingScreen(modifier = modifier)

    uiState.isChangeThemeError -> {
      if (isShowChangeThemeErrorDialog) {
        NotificationDialog(
          onDismissClick = { isShowChangeThemeErrorDialog = false },
          onConfirmClick = {
            isShowChangeThemeErrorDialog = false
            onRetry()
          },
        )
      }
    }

    uiState.isChangeThemeSuccess -> {
      if (isShowChangeThemeSuccessDialog) {
        NotificationDialog(
          icon = Icons.Default.Done,
          title = stringResource(R.string.theme_change_successful),
          isEnableDismiss = false,
          confirm = stringResource(R.string.ok),
          onConfirmClick = { isShowChangeThemeSuccessDialog = false },
        )
      }
    }
  }

  if (isShowConfirmChangeThemeDialog) {
    NotificationDialog(
      title = stringResource(R.string.are_you_sure_you_want_to_change_the_theme),
      onDismissClick = { isShowConfirmChangeThemeDialog = false },
      confirm = stringResource(R.string.change),
      onConfirmClick = {
        isShowConfirmChangeThemeDialog = false
        onChangeThemeType(selectedThemeType)
      },
    )
  }
}