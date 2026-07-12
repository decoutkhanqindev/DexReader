package com.decoutkhanqindev.dexreader.presentation.screens.reader

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.manga.ChapterPagesModel

@Immutable
sealed interface ChapterPagesUiState {
  data object Loading : ChapterPagesUiState

  @Immutable
  data class Error(val error: FeatureError = FeatureError.Generic) : ChapterPagesUiState

  @Immutable
  data class Success(
    val currentChapterPage: Int = 1,
    val chapterPages: ChapterPagesModel,
  ) : ChapterPagesUiState
}