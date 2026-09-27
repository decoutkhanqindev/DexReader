package com.decoutkhanqindev.dexreader.presentation.screens.onboarding.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.decoutkhanqindev.dexreader.ads.composables.NativeAdView
import com.decoutkhanqindev.dexreader.ads.composables.NativeLayoutType
import com.decoutkhanqindev.dexreader.presentation.model.value.onboarding.OnboardingPagerItem
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalAdsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun OnboardingContent(
  modifier: Modifier = Modifier,
  onGetStartedClick: () -> Unit,
) {
  val context = LocalContext.current
  val adsManager = LocalAdsManager.current
  val adUnits = adsManager.nativeObs
  val items = OnboardingPagerItem.entries
  val fsIndex = items.indexOf(OnboardingPagerItem.FullScreenAd)
  val pagerState = rememberPagerState(pageCount = { items.size })
  val scope = rememberCoroutineScope()
  var isCloseVisible by remember { mutableStateOf(false) }
  val isFsLocked = pagerState.currentPage == fsIndex && !isCloseVisible
  val handleNext = {
    if (pagerState.currentPage < pagerState.pageCount - 1) scope.launch {
      pagerState.animateScrollToPage(pagerState.currentPage + 1)
    }
  }

  LifecycleResumeEffect(Unit) {
    val job = scope.launch {
      snapshotFlow { pagerState.currentPage }.collectLatest { page ->
        adUnits.getOrNull(page - 1)?.load(context)
        adUnits.getOrNull(page)?.load(context)
        adUnits.getOrNull(page + 1)?.load(context)
      }
    }
    onPauseOrDispose { job.cancel() }
  }

  LifecycleResumeEffect(Unit) {
    val job = scope.launch {
      snapshotFlow { pagerState.currentPage }.collectLatest { page ->
        isCloseVisible = false
        if (page == fsIndex) {
          delay(3_000)
          isCloseVisible = true
        }
      }
    }
    onPauseOrDispose { job.cancel() }
  }

  BackHandler(true) {
    if (isFsLocked) return@BackHandler
    if (pagerState.currentPage > 0) scope.launch {
      pagerState.animateScrollToPage(pagerState.currentPage - 1)
    }
  }

  HorizontalPager(
    state = pagerState,
    userScrollEnabled = !isFsLocked,
    beyondViewportPageCount = pagerState.pageCount,
    contentPadding = PaddingValues(1.dp),
    modifier = modifier
      .background(
        Brush.radialGradient(
          colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            Color.Transparent,
          )
        )
      )
  ) { index ->
    when (val item = items[index]) {
      is OnboardingPagerItem.FullScreenAd -> {
        NativeAdView(
          adUnit = { adUnits[index] },
          layoutType = NativeLayoutType.FULL_SCREEN,
          modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
          isCloseVisible = isCloseVisible,
          onCloseClick = handleNext,
        )
      }

      is OnboardingPagerItem.Content -> {
        val isLastPage = index == items.lastIndex

        Column(
          modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          OnboardingPage(
            page = item.page,
            modifier = Modifier
              .weight(1f)
              .fillMaxWidth(),
          )

          OnboardingPageIndicator(
            pageCount = items.size,
            selectedPage = pagerState.currentPage,
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 24.dp),
          )

          OnboardingActions(
            isLastPage = isLastPage,
            modifier = Modifier
              .fillMaxWidth()
              .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
            onSkipClick = onGetStartedClick,
            onNextClick = { if (isLastPage) onGetStartedClick() else handleNext() }
          )

          NativeAdView(
            adUnit = { adUnits[index] },
            layoutType = NativeLayoutType.MEDIA_16_9,
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    }
  }
}
