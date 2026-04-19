package com.decoutkhanqindev.dexreader.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.settings.ObserveThemeModeUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.settings.SaveThemeModeUseCase
import com.decoutkhanqindev.dexreader.presentation.mapper.ThemeModeMapper.toThemeMode
import com.decoutkhanqindev.dexreader.presentation.mapper.ThemeModeMapper.toThemeModeEnum
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
            _uiState.update {
              it.copy(
                isLoading = false,
                themeOption = mode.toThemeModeEnum(),
              )
            }
          }
          .onFailure { throwable ->
            _uiState.update {
              it.copy(
                isLoading = false,
                themeOption = ThemeModeValue.SYSTEM,
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

      saveThemeModeUseCase(currentUiState.themeOption.toThemeMode())
        .onSuccess {
          _uiState.update {
            it.copy(
              isLoading = false,
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

}
