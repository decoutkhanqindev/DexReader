package com.decoutkhanqindev.dexreader.presentation.screens.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.MangaCoverArt

@Composable
fun ReadingHistoryItem(
  readingHistory: ReadingHistory,
  onSelectedReadingHistory: (
    mangaId: String,
    chapterId: String,
    lastReadPage: Int
  ) -> Unit,
  onRemoveFromHistory: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val state = rememberSwipeToDismissBoxState(
    confirmValueChange = { value ->
      if (value == SwipeToDismissBoxValue.EndToStart) {
        onRemoveFromHistory(readingHistory.id)
        false
      } else false
    }
  )
  val isSwiping = state.dismissDirection == SwipeToDismissBoxValue.EndToStart

  SwipeToDismissBox(
    state = state,
    backgroundContent = {
      SwipeToDismissBackground(
        isSwiping = isSwiping,
        modifier = Modifier
          .fillMaxSize()
          .clip(MaterialTheme.shapes.large)
      )
    },
    enableDismissFromStartToEnd = false,
    enableDismissFromEndToStart = true,
    gesturesEnabled = true,
    content = {
      Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
          onSelectedReadingHistory(
            readingHistory.mangaId,
            readingHistory.chapterId,
            readingHistory.lastReadPage
          )
        },
      ) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
          MangaCoverArt(
            url = readingHistory.mangaCoverUrl,
            title = readingHistory.mangaTitle,
            modifier = Modifier.weight(0.25f)
          )
          ReadingHistoryInfo(
            readingHistory = readingHistory,
            modifier = Modifier.weight(0.75f)
          )
        }
      }
    },
    modifier = modifier,
  )
}
