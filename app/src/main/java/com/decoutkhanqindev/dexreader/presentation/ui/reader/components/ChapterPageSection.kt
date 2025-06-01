package com.decoutkhanqindev.dexreader.presentation.ui.reader.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun ChapterPageSection(
  chapterPages: List<String>,
  currentPage: Int,
  totalPages: Int,
  onUpdateChapterPage: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val pagerState = rememberPagerState(
    initialPage = currentPage - 1,
    pageCount = { totalPages }
  )

  HorizontalPager(
    modifier = modifier,
    state = pagerState
  ) { pageIndex ->
    val currentPageIndex = pagerState.currentPage

    AsyncImage(
      model = ImageRequest.Builder(LocalContext.current)
        .data(chapterPages[pageIndex])
        .crossfade(true)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .build(),
      contentDescription = chapterPages[pageIndex],
      contentScale = ContentScale.Fit,
      modifier = Modifier.fillMaxSize(),
    )

    LaunchedEffect(currentPageIndex) {
      if (pageIndex == currentPageIndex) {
        onUpdateChapterPage(currentPageIndex + 1)
      }
    }
  }
}