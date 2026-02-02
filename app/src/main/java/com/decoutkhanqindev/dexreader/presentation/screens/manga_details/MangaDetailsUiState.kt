package com.decoutkhanqindev.dexreader.presentation.screens.manga_details

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.domain.model.Manga

sealed interface MangaDetailsUiState {
  data object Loading : MangaDetailsUiState
  data object Error : MangaDetailsUiState
  @Immutable
  data class Success(val manga: Manga) : MangaDetailsUiState
}
