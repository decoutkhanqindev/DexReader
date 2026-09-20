package com.decoutkhanqindev.dexreader.ads.ad_unit

import android.content.Context
import android.util.DisplayMetrics
import androidx.compose.runtime.Stable
import com.decoutkhanqindev.dexreader.data.network.connectivity.NetworkManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class BannerAdUnit(
  floors: List<Pair<String, String>>,
  networkManager: NetworkManager,
) : AdUnit(floors, networkManager) {

  private var _adView: AdView? = null
  val adView: AdView? get() = _adView

  override fun requestLoad(context: Context, generation: Int) {
    scope.launch {
      if (!isCurrentGeneration(generation)) return@launch

      _state.value = AdUnitState.LOADING
      Timber.tag(tag).d("$currentName - Loading")

      _adView?.destroy()

      val displayMetrics: DisplayMetrics = context.resources.displayMetrics
      val screenWidthDp = (displayMetrics.widthPixels / displayMetrics.density).toInt()
      val adSize =
        AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, screenWidthDp)

      val view = AdView(context).apply {
        adUnitId = currentId
        setAdSize(adSize)
      }
      _adView = view

      try {
        withTimeout(LOAD_TIMEOUT) { awaitLoad(view) }
        if (!isCurrentGeneration(generation)) return@launch
        Timber.tag(tag).d("$currentName - Loaded")
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

  private suspend fun awaitLoad(view: AdView): Unit = suspendCancellableCoroutine { cont ->
    view.adListener = object : AdListener() {
      override fun onAdLoaded() {
        if (cont.isActive) cont.resume(Unit)
      }

      override fun onAdFailedToLoad(error: LoadAdError) {
        if (cont.isActive) cont.resumeWithException(Exception(error.message))
      }

      override fun onAdImpression() {
        Timber.tag(tag).d("$currentName - Impression")
        _state.value = AdUnitState.IMPRESSION
      }
    }
    view.loadAd(AdRequest.Builder().build())
  }

  fun pause() {
    _adView?.pause()
  }

  fun resume() {
    _adView?.resume()
  }

  override fun destroy() {
    scope.cancel()
    _adView?.destroy()
    _adView = null
    _state.value = AdUnitState.NONE
  }
}
