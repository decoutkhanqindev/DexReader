package com.decoutkhanqindev.dexreader.presentation.screens.reader


import androidx.compose.runtime.Immutable

@Immutable
data class ResetProgressUiState(
  val isLoading: Boolean = false,
  val isSuccess: Boolean = false,
  val isError: Boolean = false,
)
