package com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.settings.ObserveThemeModeUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.settings.SaveThemeModeUseCase
import com.decoutkhanqindev.dexreader.presentation.mapper.ThemeModeMapper.toThemeMode
import com.decoutkhanqindev.dexreader.presentation.mapper.ThemeModeMapper.toThemeModeValue
import com.decoutkhanqindev.dexreader.presentation.model.value.settings.ThemeModeValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
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
            val value = mode.toThemeModeValue()
            _uiState.update {
              it.copy(
                isLoading = false,
                appliedThemeOption = value,
                selectedThemeOption = value,
              )
            }
          }
          .onFailure { throwable ->
            _uiState.update {
              it.copy(
                isLoading = false,
                appliedThemeOption = ThemeModeValue.SYSTEM,
                selectedThemeOption = ThemeModeValue.SYSTEM,
              )
            }

            Timber.tag(this::class.java.simpleName)
              .e("observeIsDynamicTheme have error: ${throwable.stackTraceToString()}")
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

      saveThemeModeUseCase(currentUiState.selectedThemeOption.toThemeMode())
        .onSuccess {
          _uiState.update {
            it.copy(
              isLoading = false,
              appliedThemeOption = currentUiState.selectedThemeOption,
              isSuccess = true,
              isError = false
            )
          }
        }
        .onFailure { throwable ->
          _uiState.update {
            it.copy(
              isLoading = false,
              isSuccess = false,
              isError = true
            )
          }
          Timber.tag(this::class.java.simpleName)
            .e("setThemeType have error: ${throwable.stackTraceToString()}")
        }
    }
  }

  fun updateThemeOption(value: ThemeModeValue) {
    if (_uiState.value.selectedThemeOption == value) return
    _uiState.update {
      it.copy(
        isLoading = false,
        selectedThemeOption = value,
        isSuccess = false,
        isError = false,
      )
    }
  }

  fun resetThemeOption() {
    _uiState.update { it.copy(selectedThemeOption = it.appliedThemeOption) }
  }

  fun retry() {
    if (_uiState.value.isError) saveThemeOption()
  }
}
