package com.decoutkhanqindev.dexreader.ads.ad_unit

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AppOpenAdUnit(
  floors: List<Pair<String, String>>,
  isNetworkAvailable: () -> Boolean,
  canRequestAds: () -> Boolean,
) : AdUnit(floors, isNetworkAvailable, canRequestAds) {

  private var _appOpenAd: AppOpenAd? = null

  override fun requestLoad(context: Context, generation: Int) {
    scope.launch {
      if (!isCurrentGeneration(generation)) return@launch

      _state.value = AdUnitState.LOADING
      Timber.tag(tag).d("$currentName - Loading")

      try {
        val ad = withTimeout(LOAD_TIMEOUT) { awaitLoad(context) }
        if (!isCurrentGeneration(generation)) return@launch
        Timber.tag(tag).d("$currentName - Loaded")
        _appOpenAd = ad
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

  private suspend fun awaitLoad(context: Context): AppOpenAd =
    suspendCancellableCoroutine { cont ->
      AppOpenAd.load(
        context,
        currentId,
        AdRequest.Builder().build(),
        object : AppOpenAd.AppOpenAdLoadCallback() {
          override fun onAdLoaded(ad: AppOpenAd) {
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
    onImpression: () -> Unit = {},
    onAdClosed: () -> Unit = {},
    adFailedToShow: () -> Unit = {},
  ) {
    val ad = _appOpenAd ?: return
    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
      override fun onAdImpression() {
        Timber.tag(tag).d("$currentName - Impression")
        _state.value = AdUnitState.IMPRESSION
        onImpression()
      }

      override fun onAdDismissedFullScreenContent() {
        Timber.tag(tag).d("$currentName - Closed")
        _appOpenAd = null
        _state.value = AdUnitState.NONE
        onAdClosed()
      }

      override fun onAdFailedToShowFullScreenContent(error: AdError) {
        Timber.tag(tag).d("$currentName - Failed to show: ${error.message}")
        _appOpenAd = null
        _state.value = AdUnitState.NONE
        adFailedToShow()
      }
    }
    ad.show(activity)
  }

  override fun releaseAd() {
    _appOpenAd = null
  }
}
