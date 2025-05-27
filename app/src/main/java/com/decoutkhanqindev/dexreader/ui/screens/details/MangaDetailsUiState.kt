package com.decoutkhanqindev.dexreader.ui.screens.details

import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.Manga

sealed interface MangaInfoUiState {
  data object Loading : MangaInfoUiState
  data object Error : MangaInfoUiState
  data class Success(val manga: Manga) : MangaInfoUiState
}

sealed interface MangaChaptersUiState {
  data object FirstPageLoading : MangaChaptersUiState
  data object FirstPageError : MangaChaptersUiState
  data class Content(
    val items: List<Chapter> = emptyList(),
    val currentPage: Int = 0,
    val nextPageState: MangaChaptersNextPageState
  ) : MangaChaptersUiState
}

enum class MangaChaptersNextPageState {
  LOADING,
  ERROR,
  IDLE,
  NO_MORE_ITEMS,
}