package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.chapters

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.manga.ChapterModel
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.onScalableClick
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun MangaChapterItem(
  lastChapter: String,
  chapter: ChapterModel,
  readingHistory: ReadingHistoryModel? = null,
  modifier: Modifier = Modifier,
  onChapterClick: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
) {
  val volume = chapter.volume
  val number = chapter.number

  Card(
    modifier = modifier.onScalableClick(shape = MaterialTheme.shapes.large) {
      onChapterClick(
        chapter.id,
        readingHistory?.lastReadPage ?: 0,
        chapter.mangaId
      )
    },
    shape = MaterialTheme.shapes.large,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    elevation = CardDefaults.cardElevation(8.dp),
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(12.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.Start,
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(modifier = Modifier.weight(0.7f)) {
          Text(
            text = stringResource(R.string.volume_chapter, volume, number),
            modifier = Modifier.padding(end = 4.dp),
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.titleMedium,
          )
          if (lastChapter == number) {
            Text(
              text = stringResource(R.string.last_chapter),
              fontWeight = FontWeight.ExtraBold,
              style = MaterialTheme.typography.titleMedium,
            )
          }
        }

        if (readingHistory != null) {
          Text(
            text = stringResource(
              R.string.reader_title,
              readingHistory.lastReadPage,
              readingHistory.pageCount
            ),
            modifier = Modifier.weight(0.3f),
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.titleMedium,
          )
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = chapter.title,
          modifier = Modifier.weight(0.6f),
          fontStyle = FontStyle.Italic,
          fontWeight = FontWeight.Bold,
          overflow = TextOverflow.Ellipsis,
          maxLines = 1,
          style = MaterialTheme.typography.bodyLarge,
        )
        Text(
          text = chapter.publishedAt,
          modifier = Modifier.weight(0.4f),
          fontStyle = FontStyle.Italic,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.End,
          style = MaterialTheme.typography.bodyLarge,
        )
      }
    }
  }
}

private val previewChapter = ChapterModel(
  id = "c-001",
  mangaId = "m-001",
  title = "Romance Dawn",
  number = "1",
  volume = "1",
  publishedAt = "2024-01-01",
)

@Preview
@Composable
private fun MangaChapterItemNoHistoryPreview() {
  DexReaderTheme {
    MangaChapterItem(
      lastChapter = "1110",
      chapter = previewChapter,
      readingHistory = null,
      modifier = Modifier.fillMaxWidth(),
      onChapterClick = { _, _, _ -> }
    )
  }
}

@Preview
@Composable
private fun MangaChapterItemWithHistoryPreview() {
  DexReaderTheme {
    MangaChapterItem(
      lastChapter = "1110",
      chapter = previewChapter,
      readingHistory = ReadingHistoryModel(
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
      ),
      modifier = Modifier.fillMaxWidth(),
      onChapterClick = { _, _, _ -> }
    )
  }
}
