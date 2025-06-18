package com.decoutkhanqindev.dexreader.presentation.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.decoutkhanqindev.dexreader.domain.model.ThemeType
import com.decoutkhanqindev.dexreader.presentation.ui.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.ui.settings.SettingsUiState

@Composable
fun SettingsContent(
  uiState: SettingsUiState,
  onChangeThemeType: (ThemeType) -> Unit,
  onSetThemeType: () -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isShowConfirmChangeThemeDialog by rememberSaveable { mutableStateOf(false) }
  var isShowChangeThemeErrorDialog by rememberSaveable { mutableStateOf(true) }
  var isShowChangeThemeSuccessDialog by rememberSaveable { mutableStateOf(true) }

  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier =
      if (uiState.isLoading) modifier.blur(8.dp)
      else modifier
  ) {
    ThemeSelectorList(
      selectedThemeType = uiState.themeType,
      onSelectedTheme = {
        isShowConfirmChangeThemeDialog = true
        onChangeThemeType(it)
      },
      modifier = Modifier
    )
  }

  when {
    uiState.isLoading -> LoadingScreen(modifier = modifier)

    uiState.isChangeThemeError -> {
      if (isShowChangeThemeErrorDialog) {
        NotificationDialog(
          title = stringResource(R.string.change_theme_failed),
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
        onSetThemeType()
      },
    )
  }
}