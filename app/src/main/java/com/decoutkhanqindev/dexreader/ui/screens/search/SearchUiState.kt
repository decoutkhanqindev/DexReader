package com.decoutkhanqindev.dexreader.ui.screens.search

import com.decoutkhanqindev.dexreader.domain.model.Manga

sealed interface SearchUiState {
  data object Idle: SearchUiState
  data object Loading : SearchUiState
  data object Error : SearchUiState
  data class Success(val results: List<Manga> = emptyList()): SearchUiState
}