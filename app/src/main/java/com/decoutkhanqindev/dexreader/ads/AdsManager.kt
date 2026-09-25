package com.decoutkhanqindev.dexreader.ads

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.decoutkhanqindev.dexreader.BuildConfig
import com.decoutkhanqindev.dexreader.MainActivity
import com.decoutkhanqindev.dexreader.ads.ad_unit.InterstitialAdUnit
import com.decoutkhanqindev.dexreader.ads.ad_unit.NativeAdUnit
import com.decoutkhanqindev.dexreader.data.network.connectivity.NetworkManager
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.collectCatching
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.withContextCatching
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class AdsManager @Inject constructor(
  private val application: Application,
  private val networkManager: NetworkManager,
) : Application.ActivityLifecycleCallbacks {
  private val tag: String get() = javaClass.simpleName

  private val consentInformation = UserMessagingPlatform.getConsentInformation(application)
  private val scope = CoroutineScope(Dispatchers.Main)
  private val isMobileAdsInitializeCalled = AtomicBoolean(false)
  private var isConsentRequested = false
  private val testDeviceIds =
    BuildConfig.ADMOB_TEST_DEVICE_IDS.split(',').filter { it.isNotBlank() }

  private val _isConsentGathered = MutableStateFlow(false)
  val isConsentGathered: StateFlow<Boolean> = _isConsentGathered.asStateFlow()

  private val _isMobileAdsInitialized = MutableStateFlow(false)
  val isMobileAdsInitialized: StateFlow<Boolean> = _isMobileAdsInitialized.asStateFlow()

  val canRequestAds: Boolean get() = consentInformation.canRequestAds()

  private var currentActivity: Activity? = null
  private var isAdShowing: Boolean = false

  val interSplash: InterstitialAdUnit by lazy {
    InterstitialAdUnit(
      floors = listOf(BuildConfig.INTER_SPLASH_ALL_ID to "inter_splash_all"),
      isNetworkAvailable = { networkManager.isAvailable.value },
      canRequestAds = consentInformation::canRequestAds,
      onShowed = { isAdShowing = true },
      onClosed = { isAdShowing = false },
      onFailedToShow = { isAdShowing = false }
    )
  }

  val nativeLang: NativeAdUnit by lazy {
    NativeAdUnit(
      floors = listOf(BuildConfig.NATIVE_LANG_ALL_ID to "native_lang_all"),
      isNetworkAvailable = { networkManager.isAvailable.value },
      canRequestAds = consentInformation::canRequestAds
    )
  }

  val nativeLangAlt: NativeAdUnit by lazy {
    NativeAdUnit(
      floors = listOf(BuildConfig.NATIVE_LANG_ALT_ALL_ID to "native_lang_alt_all"),
      isNetworkAvailable = { networkManager.isAvailable.value },
      canRequestAds = consentInformation::canRequestAds
    )
  }

  init {
    application.registerActivityLifecycleCallbacks(this)
  }

  private fun gatherConsent(activity: Activity) {
    if (canRequestAds) initializeMobileAds()

    val params = ConsentRequestParameters.Builder()
      .setTagForUnderAgeOfConsent(false)
      .apply { if (BuildConfig.DEBUG) setConsentDebugSettings(debugSettings(activity)) }
      .build()

    Timber.tag(tag).d("Requesting consent info update with params: $params")
    consentInformation.requestConsentInfoUpdate(
      activity,
      params,
      {
        UserMessagingPlatform.loadAndShowConsentFormIfRequired(
          currentActivity ?: activity
        ) { formError ->
          formError?.let { Timber.tag(tag).w("Consent form: ${it.errorCode} ${it.message}") }
          consentGatheringComplete()
        }
      },
      { requestError ->
        Timber.tag(tag).w("Consent info update: ${requestError.errorCode} ${requestError.message}")
        consentGatheringComplete()
      },
    )
  }

  private fun consentGatheringComplete() {
    if (canRequestAds) initializeMobileAds()
    _isConsentGathered.value = true
  }

  private fun initializeMobileAds() {
    if (isMobileAdsInitializeCalled.getAndSet(true)) return

    if (testDeviceIds.isNotEmpty()) {
      MobileAds.setRequestConfiguration(
        RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
      )
    }

    scope.launch {
      withContextCatching(
        context = Dispatchers.IO,
        action = {
          Timber.tag(tag).d("Initializing MobileAds...")
          MobileAds.initialize(application) { status ->
            Timber.tag(tag).d("MobileAds initialized: ${status.adapterStatusMap}")
            _isMobileAdsInitialized.value = true
          }
        },
        catch = {
          Timber.tag(tag).w("MobileAds initialization failed: ${it.message}")
          _isMobileAdsInitialized.value = false
        }
      )
    }
  }

  private fun debugSettings(context: Context): ConsentDebugSettings =
    ConsentDebugSettings.Builder(context)
      .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
      .apply { testDeviceIds.forEach(::addTestDeviceHashedId) }
      .build()

  override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    scope.launch {
      if (activity !is MainActivity || isConsentRequested) return@launch
      isConsentRequested = true
      networkManager.isAvailable.collectCatching(
        action = {
          if (!it || isMobileAdsInitializeCalled.get()) return@collectCatching
          gatherConsent(activity)
        },
        catch = { Timber.tag(tag).w("Network availability check failed: ${it.message}") }
      )
    }
  }

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
  }
}
