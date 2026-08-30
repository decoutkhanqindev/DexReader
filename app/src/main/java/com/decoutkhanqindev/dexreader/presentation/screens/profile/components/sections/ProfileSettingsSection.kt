package com.decoutkhanqindev.dexreader.presentation.screens.profile.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.value.settings.ThemeModeValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.sections.SectionHeader
import com.decoutkhanqindev.dexreader.presentation.screens.common.texts.LoadPageErrorMessage
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.settings.SettingsUiState
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun ProfileSettingsSection(
  uiState: SettingsUiState,
  modifier: Modifier = Modifier,
  onThemeOptionClick: (ThemeModeValue) -> Unit,
  onRetry: () -> Unit,
) {
  Column(modifier = modifier) {
    SectionHeader(
      icon = Icons.Default.Settings,
      title = stringResource(R.string.settings_menu_item),
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          start = 16.dp,
          end = 16.dp,
          top = 8.dp,
          bottom = 8.dp
        ),
    )

    if (uiState.isError) {
      LoadPageErrorMessage(
        message = stringResource(R.string.change_theme_failed),
        onRetryClick = onRetry,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp)
      )
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.Start,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      ThemeModeValue.entries.forEach { option ->
        ThemeOptionItem(
          item = option,
          isSelected = option == uiState.selectedThemeOption,
          modifier = Modifier.weight(1f),
          onClick = onThemeOptionClick,
        )
      }
    }
  }
}

@Preview
@Composable
private fun ProfileSettingsSectionSystemPreview() {
  DexReaderTheme {
    ProfileSettingsSection(
      uiState = SettingsUiState(
        appliedThemeOption = ThemeModeValue.SYSTEM,
        selectedThemeOption = ThemeModeValue.SYSTEM,
      ),
      modifier = Modifier.fillMaxWidth(),
      onThemeOptionClick = {},
      onRetry = {},
    )
  }
}

@Preview
@Composable
private fun ProfileSettingsSectionDarkPreview() {
  DexReaderTheme {
    ProfileSettingsSection(
      uiState = SettingsUiState(
        appliedThemeOption = ThemeModeValue.DARK,
        selectedThemeOption = ThemeModeValue.DARK,
      ),
      modifier = Modifier.fillMaxWidth(),
      onThemeOptionClick = {},
      onRetry = {},
    )
  }
}

@Preview
@Composable
private fun ProfileSettingsSectionErrorPreview() {
  DexReaderTheme {
    ProfileSettingsSection(
      uiState = SettingsUiState(
        isError = true,
        appliedThemeOption = ThemeModeValue.LIGHT,
        selectedThemeOption = ThemeModeValue.LIGHT,
      ),
      modifier = Modifier.fillMaxWidth(),
      onThemeOptionClick = {},
      onRetry = {},
    )
  }
}
