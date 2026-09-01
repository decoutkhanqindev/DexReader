package com.decoutkhanqindev.dexreader.presentation.screens.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.value.onboarding.OnboardingPageValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@Composable
fun OnboardingContent(
  modifier: Modifier = Modifier,
  onGetStartedClick: () -> Unit,
) {
  val pages: ImmutableList<OnboardingPageValue> = remember {
    OnboardingPageValue.entries.toPersistentList()
  }
  val pagerState = rememberPagerState(pageCount = { pages.size })
  val coroutineScope = rememberCoroutineScope()
  val isLastPage by remember(pages) {
    derivedStateOf { pagerState.currentPage == pages.lastIndex }
  }

  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    HorizontalPager(
      state = pagerState,
      beyondViewportPageCount = 1,
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .background(
          Brush.radialGradient(
            colors = listOf(
              MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
              Color.Transparent,
            )
          )
        )
        .statusBarsPadding()
        .padding(top = 24.dp)
    ) { index ->
      OnboardingPage(
        page = pages[index],
        modifier = Modifier.fillMaxSize(),
      )
    }

    OnboardingPageIndicator(
      pageCount = pages.size,
      selectedPage = pagerState.currentPage,
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 24.dp),
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .navigationBarsPadding()
        .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (!isLastPage) {
        Text(
          text = stringResource(R.string.skip),
          modifier = Modifier
            .onClick(shape = MaterialTheme.shapes.large) { onGetStartedClick() }
            .padding(horizontal = 24.dp, vertical = 16.dp),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontStyle = FontStyle.Italic,
          style = MaterialTheme.typography.titleMedium,
        )
      }

      ActionButton(
        isHighlighted = true,
        modifier = Modifier.then(if (isLastPage) Modifier.fillMaxWidth() else Modifier),
        backgroundColor = MaterialTheme.colorScheme.primary,
        onClick = {
          if (isLastPage) {
            onGetStartedClick()
          } else {
            coroutineScope.launch {
              pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
          }
        }
      ) {
        Text(
          text = stringResource(if (isLastPage) R.string.get_started else R.string.next),
          color = MaterialTheme.colorScheme.onPrimary,
          fontWeight = FontWeight.ExtraBold,
          style = MaterialTheme.typography.titleMedium,
        )
      }
    }
  }
}

@Preview
@Composable
private fun OnboardingContentPreview() {
  DexReaderTheme {
    OnboardingContent(
      modifier = Modifier.fillMaxSize(),
      onGetStartedClick = {},
    )
  }
}
