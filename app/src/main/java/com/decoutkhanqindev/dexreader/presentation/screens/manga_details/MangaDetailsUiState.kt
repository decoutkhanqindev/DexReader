package com.decoutkhanqindev.dexreader.presentation.screens.manga_details

import com.decoutkhanqindev.dexreader.domain.model.Manga

sealed interface MangaDetailsUiState {
  data object Loading : MangaDetailsUiState
  data object Error : MangaDetailsUiState
  data class Success(val manga: Manga) : MangaDetailsUiState
}