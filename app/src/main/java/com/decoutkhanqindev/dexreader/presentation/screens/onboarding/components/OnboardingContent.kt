package com.decoutkhanqindev.dexreader.presentation.screens.onboarding.components

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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.decoutkhanqindev.dexreader.presentation.model.value.onboarding.OnboardingPageValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalAdsManager
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
  val pages = OnboardingPageValue.entries
  val pagerState = rememberPagerState(pageCount = { pages.size })
  val scope = rememberCoroutineScope()

  LifecycleResumeEffect(Unit) {
    scope.launch {
      snapshotFlow { pagerState.currentPage }.collectLatest { page ->
        adUnits.getOrNull(page)?.load(context)
        adUnits.getOrNull(page - 1)?.load(context)
        adUnits.getOrNull(page + 1)?.load(context)
      }
    }
    onPauseOrDispose { }
  }

  HorizontalPager(
    state = pagerState,
    beyondViewportPageCount = 2,
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
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      OnboardingPage(
        page = pages[index],
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth(),
      )

      OnboardingPageIndicator(
        pageCount = pages.size,
        selectedPage = pagerState.currentPage,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 24.dp),
      )

      OnboardingActions(
        isLastPage = index == pages.lastIndex,
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
        onSkipClick = onGetStartedClick,
        onNextClick = {
          if (index == pages.lastIndex) {
            onGetStartedClick()
          } else {
            scope.launch {
              pagerState.animateScrollToPage(index + 1)
            }
          }
        }
      )

      NativeAdView(
        adUnit = { adUnits[index] },
        layoutType = NativeLayoutType.MEDIA_16_9,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}
