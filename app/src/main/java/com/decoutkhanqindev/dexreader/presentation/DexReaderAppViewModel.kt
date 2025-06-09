package com.decoutkhanqindev.dexreader.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.user.ObserveCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DexReaderAppViewModel @Inject constructor(
  private val observeCurrentUserUseCase: ObserveCurrentUserUseCase
) : ViewModel() {
  private val _isUserLoggedIn = MutableStateFlow(false)
  val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

  init {
    observeCurrentUser()
  }

  private fun observeCurrentUser() {
    viewModelScope.launch {
      observeCurrentUserUseCase().collect { userResult ->
        userResult
          .onSuccess {
            _isUserLoggedIn.value = it != null
          }
          .onFailure {
            _isUserLoggedIn.value = false
            Log.d(TAG, "observeCurrentUser have error: ${it.stackTraceToString()}")
          }
      }
    }
  }

  companion object {
    private const val TAG = "DexReaderAppViewModel"
  }
}