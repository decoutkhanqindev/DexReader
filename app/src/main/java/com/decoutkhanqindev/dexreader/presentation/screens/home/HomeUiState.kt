package com.decoutkhanqindev.dexreader.presentation.screens.home


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
sealed interface HomeUiState {
  data object Loading : HomeUiState

  @Immutable
  data class Error(val error: FeatureError = FeatureError.Generic) : HomeUiState

  @Immutable
  data class Success(
    val latestUpdatesMangaList: ImmutableList<MangaModel> = persistentListOf(),
    val trendingMangaList: ImmutableList<MangaModel> = persistentListOf(),
    val newReleaseMangaList: ImmutableList<MangaModel> = persistentListOf(),
    val topRatedMangaList: ImmutableList<MangaModel> = persistentListOf(),
  ) : HomeUiState
}
