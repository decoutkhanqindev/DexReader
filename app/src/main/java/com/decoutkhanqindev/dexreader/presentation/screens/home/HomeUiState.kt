package com.decoutkhanqindev.dexreader.presentation.screens.home


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaSectionValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

@Immutable
sealed interface HomeUiState {
  data object Loading : HomeUiState

  @Immutable
  data class Error(val error: FeatureError = FeatureError.Generic) : HomeUiState

  @Immutable
  data class Success(
    val bannerList: ImmutableList<MangaModel> = persistentListOf(),
    val mainSections: ImmutableMap<MangaSectionValue, ImmutableList<MangaModel>> = persistentMapOf(),
  ) : HomeUiState
}
