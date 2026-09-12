package com.decoutkhanqindev.dexreader.presentation.screens.profile.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.MangaCoverArt
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmerLoading
import com.decoutkhanqindev.dexreader.presentation.screens.history.components.ReadingHistoryInfo
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun ProfileHistoryItem(
  readingHistory: ReadingHistoryModel,
  onSelectedReadingHistory: (
    mangaId: String,
    chapterId: String,
    lastReadPage: Int,
  ) -> Unit,
  modifier: Modifier = Modifier,
) {
  val onSelected = remember(readingHistory.id, readingHistory.lastReadPage) {
    {
      onSelectedReadingHistory(
        readingHistory.mangaId,
        readingHistory.chapterId,
        readingHistory.lastReadPage
      )
    }
  }
  var isImageLoaded by remember { mutableStateOf(false) }

  Card(
    modifier = modifier
      .onClick(shape = MaterialTheme.shapes.medium) { onSelected() },
    shape = MaterialTheme.shapes.medium,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
    ) {
      MangaCoverArt(
        url = readingHistory.mangaCoverUrl,
        modifier = Modifier
          .weight(0.35f)
          .shimmerLoading(
            shape = MaterialTheme.shapes.medium,
            isEnable = !isImageLoaded
          ),
        onImageLoaded = { isImageLoaded = true }
      )

      ReadingHistoryInfo(
        readingHistory = readingHistory,
        modifier = Modifier.weight(0.65f)
      )
    }
  }
}

@Preview
@Composable
private fun ProfileHistoryItemPreview() {
  DexReaderTheme {
    ProfileHistoryItem(
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
      onSelectedReadingHistory = { _, _, _ -> },
      modifier = Modifier
        .width(300.dp)
        .height(184.dp)
    )
  }
}
