package com.decoutkhanqindev.dexreader.presentation.screens.privacy_policy

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen

@Composable
fun PrivacyPolicyScreen(
  modifier: Modifier = Modifier,
  onNavigateBack: () -> Unit,
) {
  val context = LocalContext.current
  val url = remember { "https://decoutkhanqindev.github.io/DexReader/privacy-policy" }
  var isLoading by remember { mutableStateOf(true) }

  val webViewClient = remember {
    object : WebViewClient() {
      override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
        super.onPageStarted(view, url, favicon)
        isLoading = true
      }

      override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        isLoading = false
      }
    }
  }

  val webView = remember {
    WebView(context).apply {
      layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT,
      )
      this.webViewClient = webViewClient
      settings.apply {
        domStorageEnabled = true
        loadWithOverviewMode = true
        useWideViewPort = true
      }
    }
  }

  val handleBack = {
    if (webView.canGoBack()) webView.goBack()
    else onNavigateBack()
  }

  DisposableEffect(Unit) {
    webView.loadUrl(url)
    onDispose { webView.destroy() }
  }

  BackHandler { handleBack() }

  BaseDetailsScreen(
    title = stringResource(R.string.privacy_policy),
    isSearchEnabled = false,
    modifier = modifier,
    onNavigateBack = handleBack,
  ) {
    if (isLoading) LoadingScreen(modifier = Modifier.fillMaxSize())
    else AndroidView(
      factory = { webView },
      modifier = Modifier.fillMaxSize(),
    )
  }
}