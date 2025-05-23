package com.decoutkhanqindev.dexreader.ui.screens.home

import com.decoutkhanqindev.dexreader.domain.model.Manga

sealed interface HomeUiState {
  data object Loading : HomeUiState
  data object Error : HomeUiState
  data class Success(
    val latestUploadedMangaList: List<Manga> = emptyList(),
    val trendingMangaList: List<Manga> = emptyList(),
    val newReleaseMangaList: List<Manga> = emptyList(),
    val completedMangaList: List<Manga> = emptyList(),
  ) : HomeUiState
}