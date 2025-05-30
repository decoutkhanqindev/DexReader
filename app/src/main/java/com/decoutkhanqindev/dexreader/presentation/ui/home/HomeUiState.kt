package com.decoutkhanqindev.dexreader.presentation.ui.home

import com.decoutkhanqindev.dexreader.domain.model.Manga

sealed interface HomeUiState {
  data object Loading : HomeUiState
  data object Error : HomeUiState
  data class Success(
    val latestUpdateMangaList: List<Manga> = emptyList(),
    val trendingMangaList: List<Manga> = emptyList(),
    val newReleaseMangaList: List<Manga> = emptyList(),
    val completedMangaList: List<Manga> = emptyList(),
  ) : HomeUiState
}