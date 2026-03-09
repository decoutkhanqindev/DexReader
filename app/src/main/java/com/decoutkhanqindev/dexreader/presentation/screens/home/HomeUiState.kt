package com.decoutkhanqindev.dexreader.presentation.screens.home

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.presentation.model.error.FeatureUiError

sealed interface HomeUiState {
  data object Loading : HomeUiState
  data class Error(val error: FeatureUiError = FeatureUiError.Generic) : HomeUiState

  @Immutable
  data class Success(
    val latestUpdatesMangaList: List<Manga> = emptyList(),
    val trendingMangaList: List<Manga> = emptyList(),
    val newReleaseMangaList: List<Manga> = emptyList(),
    val topRatedMangaList: List<Manga> = emptyList(),
  ) : HomeUiState
}
