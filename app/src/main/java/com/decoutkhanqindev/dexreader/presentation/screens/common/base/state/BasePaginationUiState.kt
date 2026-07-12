package com.decoutkhanqindev.dexreader.presentation.screens.common.base.state


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
sealed interface BasePaginationUiState<out T> {
  data object FirstPageLoading : BasePaginationUiState<Nothing>

  @Immutable
  data class FirstPageError(val error: FeatureError = FeatureError.Generic) :
    BasePaginationUiState<Nothing>

  @Immutable
  data class Content<T>(
    val currentList: ImmutableList<T> = persistentListOf(),
    val currentPage: Int = 1,
    val nextPageState: BaseNextPageState = BaseNextPageState.IDLE,
  ) : BasePaginationUiState<T>
}

