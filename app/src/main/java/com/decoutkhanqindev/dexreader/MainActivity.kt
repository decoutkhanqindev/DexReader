package com.decoutkhanqindev.dexreader

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.ComposeUiFlags
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.decoutkhanqindev.dexreader.presentation.navigation.NavGraph
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @OptIn(ExperimentalComposeUiApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ComposeUiFlags.isBypassUnfocusableComposeViewEnabled = false
    runCatching { enableEdgeToEdge() }
    runCatching { requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT }
    setContent { NavGraph() }
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
      WindowCompat.setDecorFitsSystemWindows(window, true)
      WindowCompat.getInsetsController(window, window.decorView).apply {
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        isAppearanceLightNavigationBars = false
//        hide(WindowInsetsCompat.Type.statusBars())
        hide(WindowInsetsCompat.Type.navigationBars())
      }
    }
  }
}