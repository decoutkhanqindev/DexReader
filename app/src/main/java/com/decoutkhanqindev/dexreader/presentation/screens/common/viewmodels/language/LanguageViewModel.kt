package com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.language

import com.decoutkhanqindev.dexreader.domain.usecase.settings.ObserveContentLanguageUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.settings.SaveContentLanguageUseCase
import com.decoutkhanqindev.dexreader.presentation.mapper.LanguageMapper.toLanguageValue
import com.decoutkhanqindev.dexreader.presentation.mapper.LanguageMapper.toMangaLanguage
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
  private val observeContentLanguageUseCase: ObserveContentLanguageUseCase,
  private val saveContentLanguageUseCase: SaveContentLanguageUseCase,
) : BaseViewModel() {
  private val _uiState = MutableStateFlow(LanguageUiState())
  val uiState: StateFlow<LanguageUiState> = _uiState.asStateFlow()

  init {
    observeAppliedLanguage()
  }

  private fun observeAppliedLanguage() {
    vmLaunch {
      observeContentLanguageUseCase().collect { result ->
        result
          .onSuccess { language ->
            _uiState.update { it.copy(appliedLanguage = language.toLanguageValue()) }
          }
          .onFailure { throwable ->
            Timber.tag(this::class.java.simpleName)
              .e("observeAppliedLanguage have error: ${throwable.stackTraceToString()}")
          }
      }
    }
  }

  fun updateSelectedLanguage(value: LanguageValue) {
    if (_uiState.value.selectedLanguage == value) return
    _uiState.update { it.copy(selectedLanguage = value, isError = false) }
  }

  fun saveSelectedLanguage() {
    val selectedLanguage = _uiState.value.selectedLanguage ?: return
    if (_uiState.value.isLoading) return

    vmLaunch {
      _uiState.update { it.copy(isLoading = true, isError = false) }

      saveContentLanguageUseCase(selectedLanguage.toMangaLanguage())
        .onSuccess {
          _uiState.update {
            it.copy(isLoading = false, appliedLanguage = selectedLanguage, isError = false)
          }
        }
        .onFailure { throwable ->
          _uiState.update { it.copy(isLoading = false, isError = true) }
          Timber.tag(this::class.java.simpleName)
            .e("saveSelectedLanguage have error: ${throwable.stackTraceToString()}")
        }
    }
  }

  fun retry() {
    if (_uiState.value.isError) saveSelectedLanguage()
  }
}
