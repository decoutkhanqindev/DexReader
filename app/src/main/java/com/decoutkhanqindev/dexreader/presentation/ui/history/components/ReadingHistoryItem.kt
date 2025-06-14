package com.decoutkhanqindev.dexreader.presentation.ui.history.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.presentation.ui.common.image.MangaCoverArt

@Composable
fun ReadingHistoryItem(
  readingHistory: ReadingHistory,
  onSelectedItem: (String) -> Unit,
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

  SwipeToDismissBox(
    state = state,
    backgroundContent = {
      SwipeToDismissBackground(
        isSwiping = state.dismissDirection == SwipeToDismissBoxValue.EndToStart,
        modifier = Modifier.fillMaxSize()
      )
    },
    enableDismissFromStartToEnd = false,
    enableDismissFromEndToStart = true,
    gesturesEnabled = true,
    content = {
      Card(
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(8.dp),
        onClick = { onSelectedItem(readingHistory.id) },
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        ) {
          MangaCoverArt(
            url = readingHistory.mangaCoverUrl,
            title = readingHistory.mangaTitle,
            modifier = Modifier.weight(0.25f)
          )
          ReadingHistoryInfo(
            readingHistory = readingHistory,
            modifier = Modifier
              .weight(0.75f)
              .padding(8.dp)
          )
        }
      }
    },
    modifier = modifier,
  )
}
