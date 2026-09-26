package com.decoutkhanqindev.dexreader.presentation.screens.language

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.ads.composables.NativeAdView
import com.decoutkhanqindev.dexreader.ads.composables.NativeLayoutType
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageTypeValue
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageValue
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalAdsManager
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalDataStoreManager
import com.decoutkhanqindev.dexreader.presentation.screens.language.components.LanguageContent
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateClearStack

@Composable
fun LanguageAltScreen(
  navController: NavHostController,
  langCode: String,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val adsManager = LocalAdsManager.current
  val nativeLangAlt = adsManager.nativeLangAlt
  val nativeOb1 = adsManager.nativeOb1
  val nativeOb2 = adsManager.nativeOb2
  val dataStoreManager = LocalDataStoreManager.current
  var selectedLanguage by remember { mutableStateOf(LanguageValue.fromCode(langCode)) }

  SideEffect(Unit) {
    nativeOb1.load(context)
    nativeOb2.load(context)
  }

  BackHandler { }

  BaseDetailsScreen(
    title = stringResource(R.string.language),
    isBackEnabled = false,
    isSearchEnabled = false,
    bottomBar = {
      NativeAdView(
        adUnit = { nativeLangAlt },
        layoutType = NativeLayoutType.MEDIA_4_3
      )
    },
    modifier = modifier,
  ) {
    LanguageContent(
      type = LanguageTypeValue.ALT,
      selectedLanguage = selectedLanguage,
      modifier = Modifier.fillMaxSize(),
      onLanguageClick = { selectedLanguage = it },
      onDoneClick = {
        dataStoreManager.saveSelectedLangCode(selectedLanguage.code)
        navController.navigateClearStack<NavRoute.LanguageAlt>(NavRoute.Onboarding)
      },
    )
  }
}
