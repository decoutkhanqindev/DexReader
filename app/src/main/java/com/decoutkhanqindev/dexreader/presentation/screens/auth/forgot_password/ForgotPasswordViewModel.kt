package com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.exception.UserException
import com.decoutkhanqindev.dexreader.domain.usecase.user.SendResetPasswordUseCase
import com.decoutkhanqindev.dexreader.presentation.screens.auth.AuthError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
  private val sendResetPasswordUseCase: SendResetPasswordUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(ForgotPasswordUiState())
  val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

  fun submit() {
    val currentUiState = _uiState.value

    if (currentUiState.isLoading) return

    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true, isSuccess = false, isError = false) }

      sendResetPasswordUseCase(email = currentUiState.email.trim())
        .onSuccess {
          _uiState.update { it.copy(isLoading = false, isSuccess = true, isError = false) }
        }
        .onFailure { throwable ->
          _uiState.update {
            when (throwable) {
              is UserException.Email.Empty ->
                it.copy(
                  isLoading = false,
                  emailError = AuthError.EmailError.Required
                )

              is UserException.Email.Invalid ->
                it.copy(
                  isLoading = false,
                  emailError = AuthError.EmailError.Invalid
                )

              else -> it.copy(isLoading = false, isSuccess = false, isError = true)
            }
          }

          Log.d(TAG, "submit has error: ${throwable.stackTraceToString()}")
        }
    }
  }

  fun updateEmail(value: String) {
    if (_uiState.value.email == value) return
    _uiState.update {
      it.copy(
        email = value,
        emailError = null,
        isLoading = false,
        isSuccess = false,
        isError = false
      )
    }
  }

  fun retry() {
    if (_uiState.value.isError) submit()
  }

  companion object {
    private const val TAG = "ForgotPasswordViewModel"
  }
}
