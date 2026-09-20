package com.decoutkhanqindev.dexreader.ads.composables

import android.content.Context
import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.ads.ad_unit.AdUnitState
import com.decoutkhanqindev.dexreader.ads.ad_unit.NativeAdUnit
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmerLoading
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

@Immutable
enum class NativeLayoutType { MEDIA_4_3, MEDIA_16_9 }

@Composable
fun NativeAdView(
  adUnit: () -> NativeAdUnit,
  layoutType: NativeLayoutType,
  modifier: Modifier = Modifier,
) {
  val preview = LocalInspectionMode.current
  val context = LocalContext.current
  val adState by adUnit().state.collectAsStateWithLifecycle()
  val nativeAd = adUnit().nativeAd

  DisposableEffect(Unit) {
    adUnit().load(context)
    onDispose { adUnit().release() }
  }

  if (preview) return
  if (adState == AdUnitState.NONE || adState == AdUnitState.FAILED) return

  Box(modifier = modifier.fillMaxWidth()) {
    AndroidView(
      factory = layoutType.viewBuilder(),
      onRelease = { view -> view.destroy() },
      update = { view ->
        if (nativeAd != null) bindNativeAd(view, nativeAd)
      },
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

private fun NativeLayoutType.viewBuilder(): (Context) -> NativeAdView = when (this) {
  NativeLayoutType.MEDIA_4_3 -> { context ->
    buildNativeAdView(context, R.layout.native_media_4_3_ad, iconCornerDp = 8)
  }

  NativeLayoutType.MEDIA_16_9 -> { context ->
    buildNativeAdView(context, R.layout.native_media_16_9_ad, iconCornerDp = 6)
  }
}

private fun buildNativeAdView(
  context: Context,
  @LayoutRes layoutRes: Int,
  iconCornerDp: Int,
): NativeAdView {
  val dp = { v: Int -> (v * context.resources.displayMetrics.density).toInt() }
  val view = LayoutInflater.from(context).inflate(layoutRes, null, false) as NativeAdView

  val icon = view.findViewById<ImageView>(R.id.ad_icon)
  icon.clipToOutline = true
  icon.outlineProvider = object : ViewOutlineProvider() {
    override fun getOutline(v: View, outline: Outline) {
      outline.setRoundRect(0, 0, v.width, v.height, dp(iconCornerDp).toFloat())
    }
  }

  val media = view.findViewById<MediaView>(R.id.ad_media)
  media.clipToOutline = true
  media.outlineProvider = object : ViewOutlineProvider() {
    override fun getOutline(v: View, outline: Outline) {
      outline.setRoundRect(0, 0, v.width, v.height, dp(8).toFloat())
    }
  }

  view.iconView = icon
  view.headlineView = view.findViewById(R.id.ad_headline)
  view.bodyView = view.findViewById(R.id.ad_body)
  view.mediaView = media
  view.callToActionView = view.findViewById(R.id.ad_cta)

  return view
}

private fun bindNativeAd(view: NativeAdView, nativeAd: NativeAd) {
  val icon = nativeAd.icon
  (view.iconView as? ImageView)?.apply {
    visibility = if (icon != null) View.VISIBLE else View.GONE
    if (icon != null) setImageDrawable(icon.drawable)
  }
  (view.headlineView as? TextView)?.text = nativeAd.headline
  (view.bodyView as? TextView)?.apply {
    text = nativeAd.body
    visibility = if (nativeAd.body != null) View.VISIBLE else View.GONE
  }
  view.mediaView?.visibility = run {
    val media = nativeAd.mediaContent
    if (media != null && (media.hasVideoContent() || nativeAd.images.isNotEmpty())) {
      View.VISIBLE
    } else {
      View.GONE
    }
  }
  (view.callToActionView as? TextView)?.apply {
    text = nativeAd.callToAction
    visibility = if (nativeAd.callToAction != null) View.VISIBLE else View.GONE
  }
  view.setNativeAd(nativeAd)
}
