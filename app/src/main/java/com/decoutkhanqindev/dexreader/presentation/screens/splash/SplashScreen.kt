package com.decoutkhanqindev.dexreader.presentation.screens.splash

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.decoutkhanqindev.dexreader.ads.ad_unit.AdUnitState
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalAdsManager
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalDataStoreManager
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalNetworkManager
import com.decoutkhanqindev.dexreader.presentation.screens.splash.components.SplashContent
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateClearStack

@Composable
fun SplashScreen(
  navController: NavHostController,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val activity = LocalActivity.current
  val isFirstOpen by LocalDataStoreManager.current.isFirstOpen.collectAsStateWithLifecycle()
  val isNetworkAvailable by LocalNetworkManager.current.isAvailable.collectAsStateWithLifecycle()
  val adsManager = LocalAdsManager.current
  val isConsentGathered by adsManager.isConsentGathered.collectAsStateWithLifecycle()
  val isMobileAdsInitialized by adsManager.isMobileAdsInitialized.collectAsStateWithLifecycle()
  val interSplash = adsManager.interSplash
  val interSplashState by interSplash.state.collectAsStateWithLifecycle()
  val nativeLang = adsManager.nativeLang
  val handleNext = {
    if (isFirstOpen == true) {
      navController.navigateClearStack<NavRoute.Splash>(NavRoute.LanguageSelection)
    } else {
      navController.navigateClearStack<NavRoute.Splash>(NavRoute.Main)
    }
  }

  SideEffect(isConsentGathered, isMobileAdsInitialized, isNetworkAvailable) {
    if (isConsentGathered && isMobileAdsInitialized && isNetworkAvailable) interSplash.load(context)
  }

  LifecycleResumeEffect(interSplashState, isNetworkAvailable) {
    if (isNetworkAvailable) {
      when (interSplashState) {
        AdUnitState.LOADED -> activity?.let {
          nativeLang.load(it)
          interSplash.show(
            activity = it,
            onAdShowed = handleNext,
            onAdFailedToShow = handleNext
          )
        } ?: handleNext()

        AdUnitState.FAILED -> handleNext()

        else -> Unit
      }
    }

    onPauseOrDispose { }
  }

  BackHandler { }

  SplashContent(
    isNetworkAvailable = { isNetworkAvailable },
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(32.dp)
  )
}
