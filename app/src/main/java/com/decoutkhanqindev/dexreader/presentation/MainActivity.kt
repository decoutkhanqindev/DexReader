package com.decoutkhanqindev.dexreader.presentation

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.decoutkhanqindev.dexreader.presentation.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Timber.tag(this::class.java.simpleName).d("onCreate: initializing UI")
    runCatching { enableEdgeToEdge() }
    runCatching { requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT }
    setContent { NavGraph() }
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