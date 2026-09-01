package com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.onboarding

import com.decoutkhanqindev.dexreader.domain.usecase.settings.ObserveIsOnboardingCompletedUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.settings.SaveIsOnboardingCompletedUseCase
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
  private val observeIsOnboardingCompletedUseCase: ObserveIsOnboardingCompletedUseCase,
  private val saveIsOnboardingCompletedUseCase: SaveIsOnboardingCompletedUseCase,
) : BaseViewModel() {
  private val _uiState = MutableStateFlow(OnboardingUiState())
  val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

  init {
    observeIsCompleted()
  }

  private fun observeIsCompleted() {
    vmLaunch {
      observeIsOnboardingCompletedUseCase().collect { result ->
        result
          .onSuccess { isCompleted -> _uiState.update { it.copy(isCompleted = isCompleted) } }
          .onFailure { throwable ->
            _uiState.update { it.copy(isCompleted = true) }

            Timber.tag(this::class.java.simpleName)
              .e("observeIsCompleted have error: ${throwable.stackTraceToString()}")
          }
      }
    }
  }

  fun completeOnboarding() {
    if (_uiState.value.isCompleted == true) return

    vmLaunch {
      saveIsOnboardingCompletedUseCase(true)
        .onFailure { throwable ->
          Timber.tag(this::class.java.simpleName)
            .e("completeOnboarding have error: ${throwable.stackTraceToString()}")
        }
    }
  }
}
