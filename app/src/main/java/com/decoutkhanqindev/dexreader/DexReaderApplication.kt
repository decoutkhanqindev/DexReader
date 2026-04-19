package com.decoutkhanqindev.dexreader

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class DexReaderApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    setupTimber()
    setUpApplicationLifecycleObserver()
  }

  private fun setupTimber() {
    if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
  }

  private fun setUpApplicationLifecycleObserver() {
    ProcessLifecycleOwner.get().lifecycle.addObserver(
      object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
          super.onCreate(owner)
          Timber.tag(this@DexReaderApplication::class.java.simpleName)
            .d("onCreate: app process created")
        }

        override fun onStart(owner: LifecycleOwner) {
          super.onStart(owner)
          Timber.tag(this@DexReaderApplication::class.java.simpleName)
            .d("onStart: app moved to foreground")
        }

        override fun onResume(owner: LifecycleOwner) {
          super.onResume(owner)
          Timber.tag(this@DexReaderApplication::class.java.simpleName)
            .d("onResume: app active in foreground")
        }

        override fun onPause(owner: LifecycleOwner) {
          super.onPause(owner)
          Timber.tag(this@DexReaderApplication::class.java.simpleName)
            .d("onPause: app moving to background")
        }

        override fun onStop(owner: LifecycleOwner) {
          super.onStop(owner)
          Timber.tag(this@DexReaderApplication::class.java.simpleName)
            .d("onStop: app moved to background")
        }

        override fun onDestroy(owner: LifecycleOwner) {
          super.onDestroy(owner)
          Timber.tag(this@DexReaderApplication::class.java.simpleName)
            .d("onDestroy: app process destroyed")
        }
      }
    )
  }
}