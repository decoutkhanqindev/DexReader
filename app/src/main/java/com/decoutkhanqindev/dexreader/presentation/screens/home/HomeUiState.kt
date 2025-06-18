package com.decoutkhanqindev.dexreader.presentation.screens.home

import com.decoutkhanqindev.dexreader.domain.model.Manga

sealed interface HomeUiState {
  data object Loading : HomeUiState
  data object Error : HomeUiState
  data class Success(
    val latestUpdatesMangaList: List<Manga> = emptyList(),
    val trendingMangaList: List<Manga> = emptyList(),
    val newReleaseMangaList: List<Manga> = emptyList(),
    val topRatedMangaList: List<Manga> = emptyList(),
  ) : HomeUiState
}