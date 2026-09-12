package com.decoutkhanqindev.dexreader.presentation.screens.reader.components.pages


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ChapterPagesSection(
  reset: Boolean = false,
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

  val latestOnUpdateChapterPage by rememberUpdatedState(onUpdateChapterPage)

  LaunchedEffect(Unit) {
    snapshotFlow { pagerState.currentPage }.collect { latestOnUpdateChapterPage(it + 1) }
  }

  LaunchedEffect(reset) {
    if (reset) pagerState.animateScrollToPage(0)
  }

  HorizontalPager(
    state = pagerState,
    modifier = modifier,
    beyondViewportPageCount = 2
  ) { pageIndex ->
    ChapterPageImage(
      url = chapterPages[pageIndex],
      modifier = Modifier.fillMaxSize()
    )
  }
}

@Preview
@Composable
private fun ChapterPagesSectionPreview() {
  DexReaderTheme {
    ChapterPagesSection(
      chapterPages = persistentListOf("", "", ""),
      currentPage = 1,
      totalPages = 3,
      modifier = Modifier
        .fillMaxWidth()
        .height(500.dp),
      onUpdateChapterPage = {}
    )
  }
}