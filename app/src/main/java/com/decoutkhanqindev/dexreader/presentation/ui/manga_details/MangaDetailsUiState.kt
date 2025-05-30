package com.decoutkhanqindev.dexreader.presentation.ui.manga_details

import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.Manga

sealed interface MangaDetailsUiState {
  data object Loading : MangaDetailsUiState
  data object Error : MangaDetailsUiState
  data class Success(val manga: Manga) : MangaDetailsUiState
}

sealed interface MangaChaptersUiState {
  data object FirstPageLoading : MangaChaptersUiState
  data object FirstPageError : MangaChaptersUiState
  data class Content(
    val chapterList: List<Chapter> = emptyList(),
    val currentPage: Int = 0,
    val nextPageState: MangaChaptersNextPageState = MangaChaptersNextPageState.IDLE
  ) : MangaChaptersUiState
}

enum class MangaChaptersNextPageState {
  LOADING,
  ERROR,
  IDLE,
  NO_MORE_ITEMS,
}