package com.decoutkhanqindev.dexreader.presentation.screens.language

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageTypeValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.language.LanguageViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.language.components.LanguageContent

@Composable
fun LanguageSettingScreen(
  languageViewModel: LanguageViewModel,
  modifier: Modifier = Modifier,
  onNavigateBack: () -> Unit,
) {
  val uiState by languageViewModel.uiState.collectAsStateWithLifecycle()

  BaseDetailsScreen(
    title = stringResource(R.string.language),
    isSearchEnabled = false,
    modifier = modifier,
    onNavigateBack = onNavigateBack,
  ) {
    LanguageContent(
      type = LanguageTypeValue.SETTING,
      selectedLanguage = uiState.selectedLanguage,
      appliedLanguage = uiState.appliedLanguage,
      modifier = Modifier.fillMaxSize(),
      onLanguageClick = { languageViewModel.updateSelectedLanguage(it) },
      onDoneClick = {
        languageViewModel.saveSelectedLanguage()
        onNavigateBack()
      },
    )
  }
}
