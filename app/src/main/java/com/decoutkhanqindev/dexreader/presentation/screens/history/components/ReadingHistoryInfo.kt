package com.decoutkhanqindev.dexreader.presentation.screens.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.ReadingProgressBar
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun ReadingHistoryInfo(
  readingHistory: ReadingHistoryModel,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    Text(
      text = stringResource(
        R.string.volume_chapter,
        readingHistory.chapterVolume,
        readingHistory.chapterNumber
      ),
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.primary,
      modifier = Modifier.fillMaxWidth()
    )
    Text(
      text = readingHistory.chapterTitle,
      fontStyle = FontStyle.Italic,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.labelMedium,
      modifier = Modifier.fillMaxWidth()
    )
    Text(
      text = readingHistory.mangaTitle,
      fontWeight = FontWeight.Light,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.labelMedium,
      modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.weight(1f))
    ReadingProgressBar(
      lastReadPage = readingHistory.lastReadPage,
      pageCount = readingHistory.pageCount,
      modifier = Modifier.fillMaxWidth()
    )
    Text(
      text = readingHistory.lastReadAt,
      style = MaterialTheme.typography.labelMedium,
      textAlign = TextAlign.End,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 4.dp)
    )
  }
}

private val previewReadingHistory = ReadingHistoryModel(
  id = "rh-001",
  mangaId = "m-001",
  mangaTitle = "One Piece",
  mangaCoverUrl = "",
  chapterId = "c-001",
  chapterTitle = "Romance Dawn",
  chapterNumber = "1",
  chapterVolume = "1",
  lastReadPage = 12,
  pageCount = 46,
  lastReadAt = "2 hours ago",
)

@Preview
@Composable
private fun ReadingHistoryInfoPreview() {
  DexReaderTheme {
    ReadingHistoryInfo(
      readingHistory = previewReadingHistory,
      modifier = Modifier.fillMaxWidth()
    )
  }
}