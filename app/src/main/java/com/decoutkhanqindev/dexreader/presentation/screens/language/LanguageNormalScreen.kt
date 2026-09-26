package com.decoutkhanqindev.dexreader.presentation.screens.language

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavHostController
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.ads.composables.NativeAdView
import com.decoutkhanqindev.dexreader.ads.composables.NativeLayoutType
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageTypeValue
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalAdsManager
import com.decoutkhanqindev.dexreader.presentation.screens.language.components.LanguageContent
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateClearStack

@Composable
fun LanguageNormalScreen(
  navController: NavHostController,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val adsManager = LocalAdsManager.current
  val nativeLang = adsManager.nativeLang
  val nativeLangAlt = adsManager.nativeLangAlt

  LifecycleResumeEffect(Unit) {
    nativeLangAlt.load(context)
    onPauseOrDispose { }
  }

  BackHandler { }

  BaseDetailsScreen(
    title = stringResource(R.string.language),
    isBackEnabled = false,
    isSearchEnabled = false,
    bottomBar = {
      NativeAdView(
        adUnit = { nativeLang },
        layoutType = NativeLayoutType.MEDIA_4_3
      )
    },
    modifier = modifier,
  ) {
    LanguageContent(
      type = LanguageTypeValue.NORMAL,
      modifier = Modifier.fillMaxSize(),
      onLanguageClick = {
        navController.navigateClearStack<NavRoute.LanguageNormal>(NavRoute.LanguageAlt(it.code))
      },
      onDoneClick = { },
    )
  }
}
