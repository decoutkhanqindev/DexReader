package com.decoutkhanqindev.dexreader.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.screens.settings.SettingsViewModel
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Timber.tag(this::class.java.simpleName).d("onCreate: initializing UI")
    enableEdgeToEdge()
    setContent {
      val settingsViewModel = hiltViewModel<SettingsViewModel>()
      val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

      DexReaderTheme(themeOption = settingsUiState.themeOption) {
        DexReaderApp(
          modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
        )
      }
    }
  }

  override fun onStart() {
    super.onStart()
    Timber.tag(this::class.java.simpleName).d("onStart: visible but not interactable yet")
  }

  override fun onResume() {
    super.onResume()
    Timber.tag(this::class.java.simpleName).d("onResume: active and interactable")
  }

  override fun onPause() {
    super.onPause()
    Timber.tag(this::class.java.simpleName).d("onPause: losing focus")
  }

  override fun onStop() {
    super.onStop()
    Timber.tag(this::class.java.simpleName).d("onStop: no longer visible")
  }

  override fun onDestroy() {
    super.onDestroy()
    Timber.tag(this::class.java.simpleName).d("onDestroy: cleaning up resources")
  }
}