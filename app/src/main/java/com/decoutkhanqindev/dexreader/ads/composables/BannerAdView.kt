package com.decoutkhanqindev.dexreader.ads.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.ads.ad_unit.AdUnitState
import com.decoutkhanqindev.dexreader.ads.ad_unit.BannerAdUnit
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmerLoading

@Composable
fun BannerAdView(
  adUnit: () -> BannerAdUnit,
  modifier: Modifier = Modifier,
) {
  val preview = LocalInspectionMode.current
  val context = LocalContext.current
  val adState by adUnit().state.collectAsStateWithLifecycle()
  val adView = adUnit().adView

  DisposableEffect(adUnit()) {
    adUnit().load(context)
    onDispose { adUnit().release() }
  }

  if (preview) return
  if (adState == AdUnitState.NONE || adState == AdUnitState.FAILED) return
  if (adView == null) return

  val adHeight = adView.adSize?.height ?: 50

  LifecycleResumeEffect(adUnit()) {
    if (adState == AdUnitState.LOADED || adState == AdUnitState.IMPRESSION) {
      adUnit().resume()
    }
    onPauseOrDispose {
      if (adState == AdUnitState.LOADED || adState == AdUnitState.IMPRESSION) {
        adUnit().pause()
      }
    }
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(adHeight.dp),
  ) {
    AndroidView(
      factory = { adView },
      modifier = Modifier.fillMaxWidth(),
    )

    if (adState == AdUnitState.LOADING) {
      Box(
        modifier = Modifier
          .matchParentSize()
          .shimmerLoading(backgroundColor = MaterialTheme.colorScheme.surfaceVariant),
      )
    }
  }
}
