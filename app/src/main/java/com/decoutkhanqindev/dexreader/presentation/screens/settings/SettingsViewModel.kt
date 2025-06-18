package com.decoutkhanqindev.dexreader.presentation.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.ThemeType
import com.decoutkhanqindev.dexreader.domain.usecase.settings.ObserveThemeTypeUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.settings.SetThemeTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val observeThemeTypeUseCase: ObserveThemeTypeUseCase,
  private val setThemeTypeUseCase: SetThemeTypeUseCase,
) : ViewModel() {
  private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState())
  val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

  init {
    observeThemeType()
  }

  private fun observeThemeType() {
    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true) }

      observeThemeTypeUseCase().collect { result ->
        result
          .onSuccess { type ->
            _uiState.update {
              it.copy(
                isLoading = false,
                themeType = type,
              )
            }
          }
          .onFailure {
            _uiState.update {
              it.copy(
                isLoading = false,
                themeType = ThemeType.SYSTEM,
              )
            }
            Log.e(TAG, "observeIsDynamicTheme have error: ${it.stackTraceToString()}")
          }
      }
    }
  }

  fun setThemeType() {
    val currentUiState = _uiState.value
    if (currentUiState.isLoading) return

    viewModelScope.launch {
      _uiState.update {
        it.copy(
          isLoading = true,
          isChangeThemeSuccess = false,
          isChangeThemeError = false
        )
      }

      setThemeTypeUseCase(currentUiState.themeType)
        .onSuccess {
          _uiState.update {
            it.copy(
              isLoading = false,
              isChangeThemeSuccess = true,
              isChangeThemeError = false
            )
          }
        }
        .onFailure {
          _uiState.update {
            it.copy(
              isLoading = false,
              isChangeThemeSuccess = false,
              isChangeThemeError = true
            )
          }
          Log.e(TAG, "setThemeType have error: ${it.stackTraceToString()}")
        }
    }
  }

  fun changeThemeType(type: ThemeType) {
    if (_uiState.value.themeType == type) return
    _uiState.update {
      it.copy(
        isLoading = false,
        themeType = type,
        isChangeThemeSuccess = false,
        isChangeThemeError = false
      )
    }
  }

  fun retry() {
    if (_uiState.value.isChangeThemeError) setThemeType()
  }

  companion object {
    private const val TAG = "SettingsViewModel"
  }
}