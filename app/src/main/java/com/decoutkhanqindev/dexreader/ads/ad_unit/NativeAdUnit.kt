package com.decoutkhanqindev.dexreader.ads.ad_unit

import android.content.Context
import androidx.compose.runtime.Stable
import com.decoutkhanqindev.dexreader.data.network.connectivity.NetworkManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class NativeAdUnit(
  floors: List<Pair<String, String>>,
  networkManager: NetworkManager,
) : AdUnit(floors, networkManager) {

  private var _nativeAd: NativeAd? = null
  val nativeAd: NativeAd? get() = _nativeAd

  override fun requestLoad(context: Context, generation: Int) {
    scope.launch {
      if (!isCurrentGeneration(generation)) return@launch

      _state.value = AdUnitState.LOADING
      Timber.tag(tag).d("$currentName - Loading")

      try {
        val ad = withTimeout(LOAD_TIMEOUT) { awaitLoad(context) }
        if (!isCurrentGeneration(generation)) return@launch
        Timber.tag(tag).d("$currentName - Loaded")
        _nativeAd?.destroy()
        _nativeAd = ad
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

  private suspend fun awaitLoad(context: Context): NativeAd =
    suspendCancellableCoroutine { cont ->
      AdLoader.Builder(context, currentId)
        .forNativeAd { ad ->
          if (cont.isActive) cont.resume(ad)
        }
        .withAdListener(
          object : AdListener() {
            override fun onAdFailedToLoad(error: LoadAdError) {
              if (cont.isActive) cont.resumeWithException(Exception(error.message))
            }

            override fun onAdImpression() {
              Timber.tag(tag).d("$currentName - Impression")
              _state.value = AdUnitState.IMPRESSION
            }
          }
        )
        .build()
        .loadAd(AdRequest.Builder().build())
    }

  override fun destroy() {
    scope.cancel()
    _nativeAd?.destroy()
    _nativeAd = null
    _state.value = AdUnitState.NONE
  }
}
