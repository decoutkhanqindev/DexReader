package com.decoutkhanqindev.dexreader.presentation.screens.manga_details

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel

sealed interface MangaDetailsUiState {
  data object Loading : MangaDetailsUiState
  data class Error(val error: FeatureError = FeatureError.Generic) : MangaDetailsUiState

  @Immutable
  data class Success(val manga: MangaModel) : MangaDetailsUiState
}
