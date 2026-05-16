package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun ReadingAndFavoriteButtons(
  isFavorite: Boolean,
  startedChapterId: String? = null,
  mangaId: String = "",
  continueChapter: ReadingHistoryModel? = null,
  modifier: Modifier = Modifier,
  onReadingClick: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  onFavoriteClick: () -> Unit,
) {
  val isContinueReading = continueChapter != null
  val canRead = startedChapterId != null || continueChapter != null
  val onReadingClick = remember(continueChapter, startedChapterId) {
    {
      if (isContinueReading) {
        onReadingClick(continueChapter.chapterId, continueChapter.lastReadPage, continueChapter.mangaId)
      } else onReadingClick(startedChapterId!!, 0, mangaId)
    }
  }

  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    ActionButton(
      onClick = onReadingClick,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth(),
      isEnabled = canRead,
    ) {
      Text(
        text =
          if (isContinueReading) stringResource(R.string.continue_reading)
          else stringResource(R.string.start_reading),
        color = MaterialTheme.colorScheme.inverseSurface,
        fontWeight = FontWeight.ExtraBold,
        style = MaterialTheme.typography.titleMedium,
      )
    }

    ActionButton(
      onClick = onFavoriteClick,
      backgroundColor = Color(0xFFE0245E),
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
    ) {
      Text(
        text =
          if (isFavorite) stringResource(R.string.unfavorite)
          else stringResource(R.string.favorite),
        color = Color.White,
        fontWeight = FontWeight.ExtraBold,
        style = MaterialTheme.typography.titleMedium,
      )
    }
  }
}

private val previewContinueChapter = ReadingHistoryModel(
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
private fun ReadingAndFavoriteButtonsNotStartedPreview() {
  DexReaderTheme {
    ReadingAndFavoriteButtons(
      isFavorite = false,
      startedChapterId = null,
      continueChapter = null,
      modifier = Modifier.fillMaxWidth(),
      onReadingClick = { _, _, _ -> },
      onFavoriteClick = {}
    )
  }
}

@Preview
@Composable
private fun ReadingAndFavoriteButtonsStartReadingPreview() {
  DexReaderTheme {
    ReadingAndFavoriteButtons(
      isFavorite = false,
      startedChapterId = "c-001",
      mangaId = "m-001",
      continueChapter = null,
      modifier = Modifier.fillMaxWidth(),
      onReadingClick = { _, _, _ -> },
      onFavoriteClick = {}
    )
  }
}

@Preview
@Composable
private fun ReadingAndFavoriteButtonsContinueReadingFavoritePreview() {
  DexReaderTheme {
    ReadingAndFavoriteButtons(
      isFavorite = true,
      continueChapter = previewContinueChapter,
      modifier = Modifier.fillMaxWidth(),
      onReadingClick = { _, _, _ -> },
      onFavoriteClick = {}
    )
  }
}