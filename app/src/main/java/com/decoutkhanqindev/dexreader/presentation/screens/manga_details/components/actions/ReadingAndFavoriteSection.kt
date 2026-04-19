package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.MoveToTopButton

@Composable
fun ActionsSection(
  itemsSize: Int,
  firstVisibleItemIndex: Int,
  isFavorite: Boolean,
  startedChapterId: String?,
  mangaId: String,
  continueChapter: ReadingHistoryModel?,
  modifier: Modifier = Modifier,
  onMoveToTopClick: () -> Unit,
  onReadingClick: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  onFavoriteClick: () -> Unit,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    MoveToTopButton(
      itemsSize = itemsSize,
      firstVisibleItemIndex = firstVisibleItemIndex,
      modifier = Modifier
        .align(Alignment.End)
        .padding(end = 16.dp)
    ) {
      onMoveToTopClick()
    }

    ActionButtons(
      isFavorite = isFavorite,
      startedChapterId = startedChapterId,
      mangaId = mangaId,
      continueChapter = continueChapter,
      modifier = Modifier
        .fillMaxWidth()
        .blurBackground(
          topAlpha = 0f,
          bottomAlpha = 1f,
        )
        .padding(16.dp),
      onReadingClick = onReadingClick,
      onFavoriteClick = onFavoriteClick,
    )
  }
}