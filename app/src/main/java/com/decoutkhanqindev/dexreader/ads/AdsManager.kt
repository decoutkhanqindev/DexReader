package com.decoutkhanqindev.dexreader.ads

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.decoutkhanqindev.dexreader.BuildConfig
import com.decoutkhanqindev.dexreader.ads.ad_unit.InterstitialAdUnit
import com.decoutkhanqindev.dexreader.data.network.connectivity.NetworkManager
import javax.inject.Inject

class AdsManager @Inject constructor(
  private val application: Application,
  private val networkManager: NetworkManager,
) : Application.ActivityLifecycleCallbacks {
  private var currentActivity: Activity? = null
  private var isAdShowing: Boolean = false

  val interSplash: InterstitialAdUnit by lazy {
    InterstitialAdUnit(
      floors = listOf(BuildConfig.INTER_SPLASH_ALL_ID to "inter_splash_all"),
      networkManager = networkManager,
      onShowed = { isAdShowing = true },
      onClosed = { isAdShowing = false },
      onFailedToShow = { isAdShowing = false }
    )
  }

  init {
    application.registerActivityLifecycleCallbacks(this)
  }

  override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
  override fun onActivityStarted(activity: Activity) = Unit

  override fun onActivityResumed(activity: Activity) {
    currentActivity = activity
  }

  override fun onActivityPaused(activity: Activity) {
    if (currentActivity == activity) currentActivity = null
  }

  override fun onActivityStopped(activity: Activity) = Unit
  override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
  override fun onActivityDestroyed(activity: Activity) {
    if (currentActivity == activity) currentActivity = null
    application.unregisterActivityLifecycleCallbacks(this)
  }
}
