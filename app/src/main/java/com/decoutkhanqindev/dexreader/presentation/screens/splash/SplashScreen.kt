package com.decoutkhanqindev.dexreader.presentation.screens.splash

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalDataStoreManager
import com.decoutkhanqindev.dexreader.presentation.screens.splash.components.SplashContent
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateClearStack
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  navController: NavHostController,
  modifier: Modifier = Modifier,
) {
  val isFirstOpen by LocalDataStoreManager.current.isFirstOpen.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    delay(3000L)
    if (isFirstOpen == true)
      navController.navigateClearStack<NavRoute.Splash>(NavRoute.LanguageSelection)
    else navController.navigateClearStack<NavRoute.Splash>(NavRoute.Main)
  }

  BackHandler { }

  SplashContent(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(32.dp)
      .navigationBarsPadding()
  )
}
