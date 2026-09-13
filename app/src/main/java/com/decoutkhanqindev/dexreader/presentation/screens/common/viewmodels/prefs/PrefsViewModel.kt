package com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.prefs

import com.decoutkhanqindev.dexreader.domain.usecase.prefs.ObserveThemeModeUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.prefs.SaveThemeModeUseCase
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
class PrefsViewModel @Inject constructor(
  private val observeThemeModeUseCase: ObserveThemeModeUseCase,
  private val saveThemeModeUseCase: SaveThemeModeUseCase,
) : BaseViewModel() {
  private val _data = MutableStateFlow(PrefsData())
  val data: StateFlow<PrefsData> = _data.asStateFlow()

  init {
    observeThemeOption()
  }

  private fun observeThemeOption() {
    vmLaunch {
      _data.update { it.copy(isLoading = true) }

      observeThemeModeUseCase().collect { result ->
        result
          .onSuccess { mode ->
            val value = mode.toThemeModeValue()
            _data.update {
              it.copy(
                isLoading = false,
                appliedThemeOption = value,
                selectedThemeOption = value,
              )
            }
          }
          .onFailure { throwable ->
            _data.update {
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
    val currentUiState = _data.value
    if (currentUiState.isLoading) return

    vmLaunch {
      _data.update {
        it.copy(
          isLoading = true,
          isSuccess = false,
          isError = false,
        )
      }

      saveThemeModeUseCase(currentUiState.selectedThemeOption.toThemeMode())
        .onSuccess {
          _data.update {
            it.copy(
              isLoading = false,
              appliedThemeOption = currentUiState.selectedThemeOption,
              isSuccess = true,
              isError = false
            )
          }
        }
        .onFailure { throwable ->
          _data.update {
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
    if (_data.value.selectedThemeOption == value) return
    _data.update {
      it.copy(
        isLoading = false,
        selectedThemeOption = value,
        isSuccess = false,
        isError = false,
      )
    }
  }

  fun retry() {
    if (_data.value.isError) saveThemeOption()
  }
}
