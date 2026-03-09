package com.decoutkhanqindev.dexreader.presentation.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.settings.ObserveThemeModeUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.settings.SaveThemeModeUseCase
import com.decoutkhanqindev.dexreader.presentation.mapper.ThemeMapper.toThemeMode
import com.decoutkhanqindev.dexreader.presentation.mapper.ThemeMapper.toThemeUiModel
import com.decoutkhanqindev.dexreader.presentation.model.ThemeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
  private val observeThemeModeUseCase: ObserveThemeModeUseCase,
  private val saveThemeModeUseCase: SaveThemeModeUseCase,
) : ViewModel() {
  private val _uiState = MutableStateFlow(SettingsUiState())
  val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

  init {
    observeThemeOption()
  }

  private fun observeThemeOption() {
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true) }

      observeThemeModeUseCase().collect { result ->
        result
          .onSuccess { mode ->
            _uiState.update {
              it.copy(
                isLoading = false,
                themeOption = mode.toThemeUiModel(),
              )
            }
          }
          .onFailure { throwable ->
            _uiState.update {
              it.copy(
                isLoading = false,
                themeOption = ThemeUiModel.SYSTEM,
              )
            }

            Log.e(TAG, "observeIsDynamicTheme have error: ${throwable.stackTraceToString()}")
          }
      }
    }
  }

  fun saveThemeOption() {
    val currentUiState = _uiState.value
    if (currentUiState.isLoading) return

    viewModelScope.launch {
      _uiState.update {
        it.copy(
          isLoading = true,
          isSuccess = false,
          isError = false,
        )
      }

      saveThemeModeUseCase(currentUiState.themeOption.toThemeMode())
        .onSuccess {
          _uiState.update { it.copy(isLoading = false, isSuccess = true, isError = false) }
        }
        .onFailure { throwable ->
          _uiState.update { it.copy(isLoading = false, isSuccess = false, isError = true) }
          Log.e(TAG, "setThemeType have error: ${throwable.stackTraceToString()}")
        }
    }
  }

  fun updateThemeOption(value: ThemeUiModel) {
    if (_uiState.value.themeOption == value) return
    _uiState.update {
      it.copy(
        isLoading = false,
        themeOption = value,
        isSuccess = false,
        isError = false,
      )
    }
  }

  fun retry() {
    if (_uiState.value.isError) saveThemeOption()
  }

  companion object {
    private const val TAG = "SettingsViewModel"
  }
}
