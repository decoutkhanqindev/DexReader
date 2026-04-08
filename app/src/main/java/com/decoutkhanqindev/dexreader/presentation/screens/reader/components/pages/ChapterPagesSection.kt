package com.decoutkhanqindev.dexreader.presentation.screens.reader.components.pages


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ChapterPagesSection(
  chapterPages: ImmutableList<String>,
  currentPage: Int,
  totalPages: Int,
  modifier: Modifier = Modifier,
  onUpdateChapterPage: (Int) -> Unit,
) {
  val pagerState = rememberPagerState(
    initialPage = currentPage - 1,
    pageCount = { totalPages }
  )

  LaunchedEffect(pagerState.currentPage) {
    onUpdateChapterPage(pagerState.currentPage + 1)
  }

  HorizontalPager(
    state = pagerState,
    modifier = modifier,
  ) { pageIndex ->
    ChapterPageImage(
      imageUrl = chapterPages[pageIndex],
      modifier = Modifier.fillMaxSize()
    )
  }
}