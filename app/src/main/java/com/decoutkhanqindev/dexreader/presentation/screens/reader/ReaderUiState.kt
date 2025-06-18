package com.decoutkhanqindev.dexreader.presentation.screens.reader

import com.decoutkhanqindev.dexreader.domain.model.ChapterPages

data class ChapterDetailsUiState(
  val volume: String = "",
  val chapterNumber: String = "",
  val title: String = "",
)

sealed interface ChapterPagesUiState {
  data object Loading : ChapterPagesUiState
  data object Error : ChapterPagesUiState
  data class Success(
    val currentChapterPage: Int = 1,
    val chapterPages: ChapterPages
  ) : ChapterPagesUiState
}

data class ChapterNavigationUiState(
  val currentChapterId: String,
  val previousChapterId: String? = null,
  val nextChapterId: String? = null,
  val canNavigatePrevious: Boolean = false,
  val canNavigateNext: Boolean = false,
)