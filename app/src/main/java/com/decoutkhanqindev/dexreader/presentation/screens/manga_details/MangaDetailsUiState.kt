package com.decoutkhanqindev.dexreader.presentation.screens.manga_details

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.presentation.model.error.FeatureUiError

sealed interface MangaDetailsUiState {
  data object Loading : MangaDetailsUiState
  data class Error(val error: FeatureUiError = FeatureUiError.Generic) : MangaDetailsUiState

  @Immutable
  data class Success(val manga: Manga) : MangaDetailsUiState
}
