package com.decoutkhanqindev.dexreader.presentation.screens.privacy_policy

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen

@Composable
fun PrivacyPolicyScreen(
  modifier: Modifier = Modifier.Companion,
  onNavigateBack: () -> Unit,
) {
  val context = LocalContext.current
  val url = stringResource(R.string.privacy_policy_url)
  var isLoading by remember { mutableStateOf(true) }

  val webViewClient = remember {
    object : WebViewClient() {
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
        javaScriptEnabled = true
        domStorageEnabled = true
        loadWithOverviewMode = true
        useWideViewPort = true
      }
    }
  }

  DisposableEffect(Unit) {
    onDispose { webView.destroy() }
  }

  BaseDetailsScreen(
    title = stringResource(R.string.privacy_policy),
    isSearchEnabled = false,
    modifier = modifier,
    onNavigateBack = onNavigateBack,
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      AndroidView(
        factory = { webView },
        modifier = Modifier
          .fillMaxSize()
          .background(color = Color.White),
        update = { view ->
          if (view.url != url) {
            view.loadUrl(url)
          }
        },
      )

      if (isLoading) LoadingScreen(Modifier.fillMaxSize())
    }
  }
}