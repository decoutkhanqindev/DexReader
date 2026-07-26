package com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.manga_section


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaSectionValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

@Immutable
sealed interface MangaSectionUiState {
  data object Loading : MangaSectionUiState

  @Immutable
  data class Error(val error: FeatureError = FeatureError.Generic) : MangaSectionUiState

  @Immutable
  data class Success(
    val bannerList: ImmutableList<MangaModel> = persistentListOf(),
    val mainSections: ImmutableMap<MangaSectionValue, ImmutableList<MangaModel>> = persistentMapOf(),
  ) : MangaSectionUiState
}
