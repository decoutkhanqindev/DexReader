package com.decoutkhanqindev.dexreader

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ComposeUiFlags
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalResources
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.decoutkhanqindev.dexreader.ads.AdsManager
import com.decoutkhanqindev.dexreader.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.dexreader.data.local.locale.LanguageManager
import com.decoutkhanqindev.dexreader.data.network.connectivity.NetworkManager
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageValue
import com.decoutkhanqindev.dexreader.presentation.navigation.NavGraph
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalAdsManager
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalDataStoreManager
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalLanguageManager
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalNetworkManager
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.perf.performance
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

  @Inject
  lateinit var languageManager: LanguageManager

  @Inject
  lateinit var adsManager: AdsManager

  @OptIn(ExperimentalComposeUiApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    ComposeUiFlags.isBypassUnfocusableComposeViewEnabled = false
    super.onCreate(savedInstanceState)
    runCatching { enableEdgeToEdge() }
    runCatching { requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT }
    setContent {
      val selectedLangCode by dataStoreManager.selectedLangCode.collectAsStateWithLifecycle()
      val isDark by dataStoreManager.isDark.collectAsStateWithLifecycle()
      val languageCode = LanguageValue.fromCode(selectedLangCode).code
      val configuration = remember(languageCode) {
        languageManager.configurationFor(languageCode)
      }
      val resources = remember(configuration) {
        languageManager.resourcesFor(configuration)
      }

      CompositionLocalProvider(
        LocalDataStoreManager provides dataStoreManager,
        LocalNetworkManager provides networkManager,
        LocalLanguageManager provides languageManager,
        LocalAdsManager provides adsManager,
        LocalConfiguration provides configuration,
        LocalResources provides resources,
      ) {
        DexReaderTheme(isDarkTheme = isDark ?: true) {
          NavGraph()
        }
      }
    }
    setUpFirebaseSdk()
  }

  private fun setUpFirebaseSdk() {
    lifecycleScope.launch(Dispatchers.IO) {
      runCatching {
        Firebase.crashlytics.isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
        Firebase.analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
        Firebase.performance.isPerformanceCollectionEnabled = !BuildConfig.DEBUG
      }
    }
  }

  override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    if (hasFocus) hideSystemBar()
  }

  private fun hideSystemBar() {
    runCatching {
      WindowCompat.getInsetsController(window, window.decorView).apply {
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        hide(WindowInsetsCompat.Type.navigationBars())
      }
    }
  }
}