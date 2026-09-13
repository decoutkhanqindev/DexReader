package com.decoutkhanqindev.dexreader.presentation.screens.language

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageTypeValue
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalDataStoreManager
import com.decoutkhanqindev.dexreader.presentation.screens.language.components.LanguageContent
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateBack

@Composable
fun LanguageSettingScreen(
  navController: NavHostController,
  modifier: Modifier = Modifier,
) {
  val dataStoreManager = LocalDataStoreManager.current
  val appliedLangCode by dataStoreManager.selectedLangCode.collectAsStateWithLifecycle()
  var selectedLanguage by remember { mutableStateOf<LanguageValue?>(null) }

  BaseDetailsScreen(
    title = stringResource(R.string.language),
    isSearchEnabled = false,
    modifier = modifier,
    onNavigateBack = { navController.navigateBack() },
  ) {
    LanguageContent(
      type = LanguageTypeValue.SETTING,
      selectedLanguage = selectedLanguage,
      appliedLanguage = LanguageValue.fromCode(appliedLangCode),
      modifier = Modifier.fillMaxSize(),
      onLanguageClick = { selectedLanguage = it },
      onDoneClick = {
        selectedLanguage?.let { dataStoreManager.saveSelectedLangCode(it.code) }
        navController.navigateBack()
      },
    )
  }
}
