package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.chapters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.Chapter

@Composable
fun MangaChapterItem(
  lastChapter: String,
  chapter: Chapter,
  onSelectedChapter: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val volume = chapter.volume
  val chapterNumber = chapter.chapterNumber

  Card(
    modifier = modifier,
    shape = MaterialTheme.shapes.large,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    elevation = CardDefaults.cardElevation(8.dp),
    onClick = { onSelectedChapter(chapter.id) }
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(12.dp),
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      if (lastChapter == chapterNumber) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = stringResource(R.string.volume_chapter, volume, chapterNumber),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
              .weight(0.7f)
              .fillMaxWidth()
          )
          Text(
            text = stringResource(R.string.last_chapter),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.End,
            modifier = Modifier
              .weight(0.3f)
              .fillMaxWidth()
          )
        }
      } else {
        Text(
          text = stringResource(R.string.volume_chapter, volume, chapterNumber),
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.ExtraBold,
        )
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = chapter.title,
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          modifier = Modifier
            .weight(0.6f)
            .fillMaxWidth()
        )
        Text(
          text = chapter.publishAt,
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          textAlign = TextAlign.End,
          modifier = Modifier
            .weight(0.4f)
            .fillMaxWidth()
        )
      }
    }
  }
}