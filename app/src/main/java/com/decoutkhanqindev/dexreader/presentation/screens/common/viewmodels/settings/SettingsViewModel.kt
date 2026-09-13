package com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.settings

import com.decoutkhanqindev.dexreader.domain.usecase.settings.ObserveNetworkAvailabilityUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.settings.ObserveThemeModeUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.settings.SaveThemeModeUseCase
import com.decoutkhanqindev.dexreader.presentation.mapper.ThemeModeMapper.toThemeMode
import com.decoutkhanqindev.dexreader.presentation.mapper.ThemeModeMapper.toThemeModeValue
import com.decoutkhanqindev.dexreader.presentation.model.value.settings.ThemeModeValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val observeThemeModeUseCase: ObserveThemeModeUseCase,
  private val saveThemeModeUseCase: SaveThemeModeUseCase,
  private val observeNetworkAvailabilityUseCase: ObserveNetworkAvailabilityUseCase,
) : BaseViewModel() {
  private val _uiState = MutableStateFlow(SettingsUiState())
  val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

  private val _isNetworkAvailable = MutableStateFlow(true)
  val isNetworkAvailable: StateFlow<Boolean> = _isNetworkAvailable.asStateFlow()

  init {
    observeThemeOption()
    observeNetworkAvailability()
  }

  private fun observeNetworkAvailability() {
    vmLaunch {
      observeNetworkAvailabilityUseCase().collect { result ->
        result
          .onSuccess { _isNetworkAvailable.value = it }
          .onFailure { throwable ->
            _isNetworkAvailable.value = true
            Timber.tag(this::class.java.simpleName)
              .e("observeNetworkAvailability have error: ${throwable.stackTraceToString()}")
          }
      }
    }
  }

  private fun observeThemeOption() {
    vmLaunch {
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
                appliedThemeOption = ThemeModeValue.DARK,
                selectedThemeOption = ThemeModeValue.DARK,
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

    vmLaunch {
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

  fun retry() {
    if (_uiState.value.isError) saveThemeOption()
  }
}
