package com.decoutkhanqindev.dexreader.ui.screens.details

import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.Manga

sealed interface MangaDetailsInfoUiState {
  data object Loading : MangaDetailsInfoUiState
  data object Error : MangaDetailsInfoUiState
  data class Success(val manga: Manga) : MangaDetailsInfoUiState
}

sealed interface MangaDetailsChaptersUiState {
  data object FirstPageLoading : MangaDetailsChaptersUiState
  data object FirstPageError : MangaDetailsChaptersUiState
  data class Content(
    val chapterList: List<Chapter> = emptyList(),
    val currentPage: Int = 0,
    val nextPageState: MangaDetailsChaptersNextPageState = MangaDetailsChaptersNextPageState.IDLE
  ) : MangaDetailsChaptersUiState
}

enum class MangaDetailsChaptersNextPageState {
  LOADING,
  ERROR,
  IDLE,
  NO_MORE_ITEMS,
}