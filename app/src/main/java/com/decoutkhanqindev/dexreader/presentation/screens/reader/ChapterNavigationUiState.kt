package com.decoutkhanqindev.dexreader.presentation.screens.reader

import androidx.compose.runtime.Immutable

@Immutable
data class ChapterNavigationUiState(
  val currentChapterId: String,
  val previousChapterId: String? = null,
  val nextChapterId: String? = null,
  val canNavigatePrevious: Boolean = false,
  val canNavigateNext: Boolean = false,
)