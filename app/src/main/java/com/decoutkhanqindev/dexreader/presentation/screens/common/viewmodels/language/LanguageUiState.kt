package com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.language

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.value.language.AppLanguageValue

@Immutable
data class LanguageUiState(
  val isLoading: Boolean = false,
  val appliedLanguage: AppLanguageValue = AppLanguageValue.DEFAULT,
  val selectedLanguage: AppLanguageValue? = null,
  val isError: Boolean = false,
)
