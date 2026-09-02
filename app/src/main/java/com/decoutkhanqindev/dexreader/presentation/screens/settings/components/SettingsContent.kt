package com.decoutkhanqindev.dexreader.presentation.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.value.settings.SettingItemValue
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun SettingsContent(
  isDarkTheme: Boolean,
  modifier: Modifier = Modifier,
  onDarkThemeChange: (Boolean) -> Unit,
  onNavigateToLanguageScreen: () -> Unit,
  onNavigateToPrivacyScreen: () -> Unit,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    SettingItemValue.entries.forEach { item ->
      when (item) {
        SettingItemValue.THEME -> SettingRow(
          item = item,
          modifier = Modifier.fillMaxWidth(),
        ) {
          Switch(
            checked = isDarkTheme,
            onCheckedChange = onDarkThemeChange,
            modifier = Modifier.size(width = 48.dp, height = 24.dp)
          )
        }

        SettingItemValue.LANGUAGE -> SettingRow(
          item = item,
          modifier = Modifier.fillMaxWidth(),
          onClick = onNavigateToLanguageScreen,
        ) { ChevronIcon() }

        SettingItemValue.PRIVACY -> SettingRow(
          item = item,
          modifier = Modifier.fillMaxWidth(),
          onClick = onNavigateToPrivacyScreen,
        ) { ChevronIcon() }
      }
    }
  }
}

@Preview
@Composable
private fun SettingsContentDarkPreview() {
  DexReaderTheme {
    SettingsContent(
      isDarkTheme = true,
      modifier = Modifier.fillMaxSize(),
      onDarkThemeChange = {},
      onNavigateToLanguageScreen = {},
      onNavigateToPrivacyScreen = {},
    )
  }
}
