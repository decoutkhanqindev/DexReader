package com.decoutkhanqindev.dexreader.ads.ad_unit

import android.app.Activity
import android.content.Context
import com.decoutkhanqindev.dexreader.data.network.connectivity.NetworkManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class InterstitialAdUnit(
  floors: List<Pair<String, String>>,
  networkManager: NetworkManager,
  private val onShowed: () -> Unit = {},
  private val onClosed: () -> Unit = {},
  private val onFailedToShow: () -> Unit = {},
) : AdUnit(floors, networkManager) {

  private var _interstitialAd: InterstitialAd? = null
  private var currentTabCount = 0
  private var lastShowTime = 0L

  fun incrementTabCount() {
    currentTabCount++
  }

  fun readyToLoad(): Boolean {
    val now = System.currentTimeMillis()
    val intervalPassed = lastShowTime == 0L || now - lastShowTime >= INTERVAL
    return currentTabCount >= TAB_THRESHOLD && intervalPassed
  }

  override fun requestLoad(context: Context, generation: Int) {
    scope.launch {
      if (!isCurrentGeneration(generation)) return@launch

      _state.value = AdUnitState.LOADING
      Timber.tag(tag).d("$currentName - Loading")

      try {
        val ad = withTimeout(LOAD_TIMEOUT) { awaitLoad(context) }
        if (!isCurrentGeneration(generation)) return@launch
        Timber.tag(tag).d("$currentName - Loaded")
        _interstitialAd = ad
        _state.value = AdUnitState.LOADED
      } catch (e: TimeoutCancellationException) {
        if (!isCurrentGeneration(generation)) return@launch
        Timber.tag(tag).d("$currentName - Timeout")
        onLoadFailed(context, generation)
      } catch (e: CancellationException) {
        throw e
      } catch (e: Exception) {
        if (!isCurrentGeneration(generation)) return@launch
        Timber.tag(tag).d("$currentName - Failed: ${e.message}")
        onLoadFailed(context, generation)
      }
    }
  }

  private suspend fun awaitLoad(context: Context): InterstitialAd =
    suspendCancellableCoroutine { cont ->
      InterstitialAd.load(
        context,
        currentId,
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
          override fun onAdLoaded(ad: InterstitialAd) {
            if (cont.isActive) cont.resume(ad)
          }

          override fun onAdFailedToLoad(error: LoadAdError) {
            if (cont.isActive) cont.resumeWithException(Exception(error.message))
          }
        },
      )
    }

  fun show(
    activity: Activity,
    onAdShowed: () -> Unit = {},
    onAdImpression: () -> Unit = {},
    onAdClosed: () -> Unit = {},
    onAdFailedToShow: () -> Unit = {},
  ) {
    val ad = _interstitialAd ?: return

    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
      override fun onAdShowedFullScreenContent() {
        Timber.tag(tag).d("$currentName - Showed")
        onAdShowed()
        onShowed()
      }

      override fun onAdImpression() {
        Timber.tag(tag).d("$currentName - Impression")
        lastShowTime = System.currentTimeMillis()
        currentTabCount = 0
        _state.value = AdUnitState.IMPRESSION
        onAdImpression()
        onShowed()
      }

      override fun onAdDismissedFullScreenContent() {
        Timber.tag(tag).d("$currentName - Closed")
        _interstitialAd = null
        _state.value = AdUnitState.NONE
        onAdClosed()
        onClosed()
      }

      override fun onAdFailedToShowFullScreenContent(error: AdError) {
        Timber.tag(tag).d("$currentName - Failed to show: ${error.message}")
        _interstitialAd = null
        _state.value = AdUnitState.NONE
        onAdFailedToShow()
        onFailedToShow()
      }
    }

    ad.show(activity)
  }

  override fun destroy() {
    scope.cancel()
    _interstitialAd = null
    _state.value = AdUnitState.NONE
  }

  companion object {
    private const val TAB_THRESHOLD = 3
    private const val INTERVAL = 60_000L
  }
}
