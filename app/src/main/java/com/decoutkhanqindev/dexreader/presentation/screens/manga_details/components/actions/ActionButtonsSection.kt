package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton

@Composable
fun ActionButtonsSection(
  startedChapterId: String? = null,
  mangaId: String = "",
  continueChapter: ReadingHistoryModel? = null,
  onReadingClick: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  isFavorite: Boolean,
  onFavoriteClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val isContinueReading = continueChapter != null
  val canRead = startedChapterId != null || continueChapter != null

  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    ActionButton(
      enabled = canRead,
      onClick = {
        if (isContinueReading) {
          onReadingClick(
            continueChapter.chapterId,
            continueChapter.lastReadPage,
            continueChapter.mangaId
          )
        } else onReadingClick(
          startedChapterId!!,
          0,
          mangaId
        )
      },
      content = {
        Text(
          text =
            if (isContinueReading) stringResource(R.string.continue_reading)
            else stringResource(R.string.start_reading),
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.inverseSurface,
          fontWeight = FontWeight.ExtraBold,
        )
      },
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
    )
    ActionButton(
      onClick = onFavoriteClick,
      content = {
        Text(
          text =
            if (isFavorite) stringResource(R.string.unfavorite)
            else stringResource(R.string.favorite),
          color = MaterialTheme.colorScheme.inverseSurface,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.ExtraBold,
        )
      },
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
    )
  }
}