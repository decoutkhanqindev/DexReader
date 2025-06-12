package com.decoutkhanqindev.dexreader.presentation.ui.auth.forgot_password

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.user.SendResetUserPasswordUseCase
import com.decoutkhanqindev.dexreader.presentation.ui.auth.AuthError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
  private val resetUserPasswordUseCase: SendResetUserPasswordUseCase
) : ViewModel() {
  private val _uiState = MutableStateFlow<ForgotPasswordUiState>(ForgotPasswordUiState())
  val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

  fun sendResetUserPassword() {
    val currentUiState = _uiState.value
    if (currentUiState.isLoading) return

    viewModelScope.launch {
      _uiState.update {
        it.copy(
          isLoading = true,
          isSuccess = false,
          isError = false
        )
      }

      if (!currentUiState.isValidEmail) {
        _uiState.update {
          it.copy(
            isLoading = false,
            isSuccess = false,
          )
        }
        return@launch
      }

      val sendResetUserPasswordResult = resetUserPasswordUseCase(email = currentUiState.email)
      sendResetUserPasswordResult
        .onSuccess {
          _uiState.update {
            it.copy(
              isLoading = false,
              isSuccess = true,
              isError = false,
            )
          }
        }
        .onFailure {
          _uiState.update {
            it.copy(
              isLoading = false,
              isSuccess = false,
              isError = true
            )
          }
          Log.d(TAG, "sendResetUserPassword have error: ${it.stackTraceToString()}")
        }
    }
  }

  fun updateEmailField(email: String) {
    if (_uiState.value.email == email) return
    _uiState.update {
      it.copy(
        email = email,
        emailError = when {
          email.isBlank() -> AuthError.EmailError.Required
          !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> AuthError.EmailError.Invalid
          else -> AuthError.UnknownError
        },
        isValidEmail = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches(),
        isLoading = false,
        isSuccess = false,
        isError = false
      )
    }
  }

  fun retry() {
    if (_uiState.value.isError) sendResetUserPassword()
  }

  companion object {
    private const val TAG = "ForgotPasswordViewModel"
  }
}