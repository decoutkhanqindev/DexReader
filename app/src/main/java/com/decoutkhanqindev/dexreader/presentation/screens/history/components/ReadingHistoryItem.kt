package com.decoutkhanqindev.dexreader.presentation.screens.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.animateItemOnAppear
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.MangaCoverArt
import com.decoutkhanqindev.dexreader.presentation.screens.common.onScalableClick
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmer
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun ReadingHistoryItem(
  readingHistory: ReadingHistoryModel,
  onSelectedReadingHistory: (
    mangaId: String,
    chapterId: String,
    lastReadPage: Int,
  ) -> Unit,
  onRemoveFromHistory: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val onRemoveFromHistory = remember(readingHistory.id) {
    { onRemoveFromHistory(readingHistory.id) }
  }
  val onSelectedReadingHistory = remember(readingHistory.id, readingHistory.lastReadPage) {
    {
      onSelectedReadingHistory(
        readingHistory.mangaId,
        readingHistory.chapterId,
        readingHistory.lastReadPage
      )
    }
  }
  val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
    confirmValueChange = { value ->
      if (value == SwipeToDismissBoxValue.EndToStart) {
        onRemoveFromHistory()
        false
      } else false
    }
  )
  val isSwiping by remember {
    derivedStateOf {
      swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart
    }
  }
  var isImageLoaded by remember { mutableStateOf(false) }

  SwipeToDismissBox(
    state = swipeToDismissBoxState,
    backgroundContent = {
      SwipeToDismissBackground(
        isSwiping = isSwiping,
        modifier = Modifier.fillMaxSize()
      )
    },
    enableDismissFromStartToEnd = false,
    enableDismissFromEndToStart = true,
    gesturesEnabled = true,
    modifier = modifier.animateItemOnAppear(),
  ) {
    Card(
      modifier = Modifier
        .fillMaxSize()
        .shimmer(isEnable = !isImageLoaded),
      shape = RoundedCornerShape(0.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      elevation = CardDefaults.cardElevation(8.dp),
    ) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp)
          .onScalableClick(shape = RoundedCornerShape(0.dp)) {
            onSelectedReadingHistory()
          }
      ) {
        MangaCoverArt(
          url = readingHistory.mangaCoverUrl,
          title = readingHistory.mangaTitle,
          modifier = Modifier.weight(0.25f),
          onImageLoaded = { isImageLoaded = true }
        )

        ReadingHistoryInfo(
          readingHistory = readingHistory,
          modifier = Modifier.weight(0.75f)
        )
      }
    }
  }
}

@Preview
@Composable
private fun ReadingHistoryItemPreview() {
  DexReaderTheme {
    ReadingHistoryItem(
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
      onRemoveFromHistory = {},
      modifier = Modifier
        .fillMaxWidth()
        .height(160.dp)
    )
  }
}
