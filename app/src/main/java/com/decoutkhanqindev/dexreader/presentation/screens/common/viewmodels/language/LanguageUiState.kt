package com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.language

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageValue

@Immutable
data class LanguageUiState(
  val isLoading: Boolean = false,
  val appliedLanguage: LanguageValue = LanguageValue.DEFAULT,
  val selectedLanguage: LanguageValue? = null,
  val isError: Boolean = false,
)
