package com.decoutkhanqindev.dexreader.presentation.ui.common.viewmodel

data class ChapterNavigationState(
  val currentChapterId: String,
  val previousChapterId: String?,
  val nextChapterId: String?,
  val canLoadNextChapterPages: Boolean
)