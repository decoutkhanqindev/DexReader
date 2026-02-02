package com.decoutkhanqindev.dexreader.presentation.screens.reader

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.domain.model.ChapterPages

@Immutable
data class ChapterDetailsUiState(
  val volume: String = "",
  val chapterNumber: String = "",
  val title: String = "",
)

sealed interface ChapterPagesUiState {
  data object Loading : ChapterPagesUiState
  data object Error : ChapterPagesUiState

  @Immutable
  data class Success(
    val currentChapterPage: Int = 1,
    val chapterPages: ChapterPages,
  ) : ChapterPagesUiState
}

@Immutable
data class ChapterNavigationUiState(
  val currentChapterId: String,
  val previousChapterId: String? = null,
  val nextChapterId: String? = null,
  val canNavigatePrevious: Boolean = false,
  val canNavigateNext: Boolean = false,
)
