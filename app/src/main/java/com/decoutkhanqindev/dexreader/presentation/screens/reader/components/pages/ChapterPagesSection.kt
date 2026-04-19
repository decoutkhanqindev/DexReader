package com.decoutkhanqindev.dexreader.presentation.screens.reader.components.pages


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

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