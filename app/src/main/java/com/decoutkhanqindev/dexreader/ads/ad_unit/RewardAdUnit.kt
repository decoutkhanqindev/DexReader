package com.decoutkhanqindev.dexreader.ads.ad_unit

import android.app.Activity
import android.content.Context
import com.decoutkhanqindev.dexreader.data.network.connectivity.NetworkManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RewardAdUnit(
  floors: List<Pair<String, String>>,
  networkManager: NetworkManager,
  private val onShowed: () -> Unit = {},
  private val onClosed: () -> Unit = {},
  private val onFailedToShow: () -> Unit = {},
) : AdUnit(floors, networkManager) {

  private var _rewardedAd: RewardedAd? = null

  override fun requestLoad(context: Context, generation: Int) {
    scope.launch {
      if (!isCurrentGeneration(generation)) return@launch

      _state.value = AdUnitState.LOADING
      Timber.tag(tag).d("$currentName - Loading")

      try {
        val ad = withTimeout(LOAD_TIMEOUT) { awaitLoad(context) }
        if (!isCurrentGeneration(generation)) return@launch
        Timber.tag(tag).d("$currentName - Loaded")
        _rewardedAd = ad
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

  private suspend fun awaitLoad(context: Context): RewardedAd =
    suspendCancellableCoroutine { cont ->
      RewardedAd.load(
        context,
        currentId,
        AdRequest.Builder().build(),
        object : RewardedAdLoadCallback() {
          override fun onAdLoaded(ad: RewardedAd) {
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
    onAdEarned: () -> Unit = {},
    onAdClosed: () -> Unit = {},
    onAdFailedToShow: () -> Unit = {},
  ) {
    val ad = _rewardedAd ?: return

    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
      override fun onAdShowedFullScreenContent() {
        Timber.tag(tag).d("$currentName - Showed")
        onShowed()
      }

      override fun onAdImpression() {
        Timber.tag(tag).d("$currentName - Impression")
        _state.value = AdUnitState.IMPRESSION
        onShowed()
      }

      override fun onAdDismissedFullScreenContent() {
        Timber.tag(tag).d("$currentName - Closed")
        _rewardedAd = null
        _state.value = AdUnitState.NONE
        onAdClosed()
        onClosed()
      }

      override fun onAdFailedToShowFullScreenContent(error: AdError) {
        Timber.tag(tag).d("$currentName - Failed to show: ${error.message}")
        _rewardedAd = null
        _state.value = AdUnitState.NONE
        onAdFailedToShow()
        onFailedToShow()
      }
    }

    ad.show(activity) {
      Timber.tag(tag).d("$currentName - Reward earned")
      onAdEarned()
      onShowed()
    }
  }

  override fun destroy() {
    scope.cancel()
    _rewardedAd = null
    _state.value = AdUnitState.NONE
  }
}
