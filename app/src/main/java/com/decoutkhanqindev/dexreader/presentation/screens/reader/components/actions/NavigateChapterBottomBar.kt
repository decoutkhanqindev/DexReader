package com.decoutkhanqindev.dexreader.presentation.screens.reader.components.actions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.ReadingProgressBar
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun NavigateChapterBottomBar(
  lastReadPage: Int,
  pageCount: Int,
  canNavigatePrevious: Boolean,
  canNavigateNext: Boolean,
  modifier: Modifier = Modifier,
  onNavigatePrevious: () -> Unit,
  onNavigateNext: () -> Unit,
) {
  BottomAppBar(
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.surfaceContainer,
    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
  ) {
    IconButton(
      onClick = onNavigatePrevious,
      modifier = Modifier.weight(0.5f),
      enabled = canNavigatePrevious,
    ) {
      Icon(
        imageVector = Icons.Default.ChevronLeft,
        contentDescription = stringResource(R.string.pre_chapter)
      )
    }
    ReadingProgressBar(
      lastReadPage = lastReadPage,
      pageCount = pageCount,
      modifier = Modifier
        .weight(2f)
        .fillMaxWidth()
    )
    IconButton(
      onClick = onNavigateNext,
      modifier = Modifier.weight(0.5f),
      enabled = canNavigateNext,
    ) {
      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = stringResource(R.string.next_chapter)
      )
    }
  }
}

@Preview
@Composable
private fun NavigateChapterBottomBarBothEnabledPreview() {
  DexReaderTheme {
    NavigateChapterBottomBar(
      lastReadPage = 12,
      pageCount = 46,
      canNavigatePrevious = true,
      canNavigateNext = true,
      onNavigatePrevious = {},
      onNavigateNext = {}
    )
  }
}

@Preview
@Composable
private fun NavigateChapterBottomBarNextOnlyPreview() {
  DexReaderTheme {
    NavigateChapterBottomBar(
      lastReadPage = 1,
      pageCount = 20,
      canNavigatePrevious = false,
      canNavigateNext = true,
      onNavigatePrevious = {},
      onNavigateNext = {}
    )
  }
}

@Preview
@Composable
private fun NavigateChapterBottomBarPreviousOnlyPreview() {
  DexReaderTheme {
    NavigateChapterBottomBar(
      lastReadPage = 46,
      pageCount = 46,
      canNavigatePrevious = true,
      canNavigateNext = false,
      onNavigatePrevious = {},
      onNavigateNext = {}
    )
  }
}
