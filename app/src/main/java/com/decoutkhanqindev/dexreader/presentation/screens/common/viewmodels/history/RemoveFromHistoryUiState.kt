package com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.history


import androidx.compose.runtime.Immutable

@Immutable
data class RemoveFromHistoryUiState(
  val isLoading: Boolean = false,
  val readingHistoryId: String? = null,
  val isSuccess: Boolean = false,
  val isError: Boolean = false,
)
