package com.decoutkhanqindev.dexreader.presentation.screens.reader


import androidx.compose.runtime.Immutable

@Immutable
data class ChapterDetailsUiState(
  val volume: String = "",
  val chapterNumber: String = "",
  val title: String = "",
)

