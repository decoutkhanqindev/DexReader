package com.decoutkhanqindev.dexreader.presentation.screens.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory

@Composable
fun ReadingHistoryInfo(
  readingHistory: ReadingHistory,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = stringResource(
          R.string.volume_chapter,
          readingHistory.chapterVolume,
          readingHistory.chapterNumber
        ),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier
          .weight(0.7f)
          .fillMaxWidth()
      )
      Text(
        text = stringResource(
          R.string.reader_title,
          readingHistory.lastReadPage,
          readingHistory.totalChapterPages
        ),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.End,
        modifier = Modifier
          .weight(0.3f)
          .fillMaxWidth()
      )
    }
    Text(
      text = readingHistory.chapterTitle,
      fontWeight = FontWeight.Bold,
      fontStyle = FontStyle.Italic,
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.bodyLarge,
      modifier = Modifier.fillMaxWidth()
    )
    Text(
      text = "(${readingHistory.mangaTitle})",
      fontWeight = FontWeight.Light,
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.bodyLarge,
      modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.weight(1f))
    Text(
      text = readingHistory.lastReadAt.toString(),
      style = MaterialTheme.typography.bodyLarge,
      fontWeight = FontWeight.Bold,
      fontStyle = FontStyle.Italic,
      textAlign = TextAlign.End,
      modifier = Modifier.fillMaxWidth()
    )
  }
}