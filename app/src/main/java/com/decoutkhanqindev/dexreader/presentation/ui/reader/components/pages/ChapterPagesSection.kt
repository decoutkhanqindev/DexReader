package com.decoutkhanqindev.dexreader.presentation.ui.reader.components.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

@Composable
fun ChapterPagesSection(
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
    state = pagerState,
  ) { pageIndex ->
    ChapterPageImage(
      imageUrl = chapterPages[pageIndex],
      modifier = Modifier.fillMaxSize()
    )
  }

  LaunchedEffect(pagerState.currentPage) {
    onUpdateChapterPage(pagerState.currentPage + 1)
  }
}