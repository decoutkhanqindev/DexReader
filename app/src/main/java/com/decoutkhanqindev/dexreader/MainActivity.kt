package com.decoutkhanqindev.dexreader

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ComposeUiFlags
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.decoutkhanqindev.dexreader.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.dexreader.data.network.connectivity.NetworkManager
import com.decoutkhanqindev.dexreader.presentation.navigation.NavGraph
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalDataStoreManager
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalNetworkManager
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @Inject
  lateinit var dataStoreManager: DataStoreManager

  @Inject
  lateinit var networkManager: NetworkManager

  @OptIn(ExperimentalComposeUiApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    ComposeUiFlags.isBypassUnfocusableComposeViewEnabled = false
    super.onCreate(savedInstanceState)
    runCatching { enableEdgeToEdge() }
    runCatching { requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT }
    setContent {
      CompositionLocalProvider(
        LocalDataStoreManager provides dataStoreManager,
        LocalNetworkManager provides networkManager,
      ) {
        NavGraph()
      }
    }
    setUpFirebaseSdk()
  }

  private fun setUpFirebaseSdk() {
    lifecycleScope.launch(Dispatchers.IO) {
      runCatching {
        Firebase.crashlytics.isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
        Firebase.analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
      }
    }
  }

  override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    if (hasFocus) hideSystemBar()
  }

  private fun hideSystemBar() {
    runCatching {
      WindowCompat.setDecorFitsSystemWindows(window, false)
      WindowCompat.getInsetsController(window, window.decorView).apply {
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        isAppearanceLightNavigationBars = false
        hide(WindowInsetsCompat.Type.navigationBars())
      }
    }
  }
}