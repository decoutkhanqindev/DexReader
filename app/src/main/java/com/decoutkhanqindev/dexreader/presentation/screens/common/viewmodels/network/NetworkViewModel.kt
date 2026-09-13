package com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.network

import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.network.ObserveNetworkAvailabilityUseCase
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
  observeNetworkAvailabilityUseCase: ObserveNetworkAvailabilityUseCase,
) : BaseViewModel() {
  val isAvailable: StateFlow<Boolean> = observeNetworkAvailabilityUseCase()
    .map { result ->
      result.onFailure { throwable ->
        Timber.tag(this::class.java.simpleName)
          .e("observeNetworkAvailability have error: ${throwable.stackTraceToString()}")
      }.getOrDefault(true)
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
      initialValue = true,
    )

  companion object {
    private const val STOP_TIMEOUT_MILLIS = 5_000L
  }
}
