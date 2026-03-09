package com.decoutkhanqindev.dexreader.presentation.screens.home


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.MangaUiModel
import com.decoutkhanqindev.dexreader.presentation.model.error.FeatureUiError
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed interface HomeUiState {
  data object Loading : HomeUiState
  data class Error(val error: FeatureUiError = FeatureUiError.Generic) : HomeUiState

  @Immutable
  data class Success(
    val latestUpdatesMangaList: ImmutableList<MangaUiModel> = persistentListOf(),
    val trendingMangaList: ImmutableList<MangaUiModel> = persistentListOf(),
    val newReleaseMangaList: ImmutableList<MangaUiModel> = persistentListOf(),
    val topRatedMangaList: ImmutableList<MangaUiModel> = persistentListOf(),
  ) : HomeUiState
}
