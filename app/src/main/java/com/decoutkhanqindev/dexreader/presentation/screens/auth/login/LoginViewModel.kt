package com.decoutkhanqindev.dexreader.presentation.screens.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.exception.UserException
import com.decoutkhanqindev.dexreader.domain.usecase.user.LoginUseCase
import com.decoutkhanqindev.dexreader.presentation.screens.auth.AuthError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val userCase: LoginUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(LoginUiState())
  val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

  fun submit() {
    val currentUiState = _uiState.value

    if (currentUiState.isLoading) return

    val currentEmail = currentUiState.email.trim()
    val currentPassword = currentUiState.password.trim()

    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true, isSuccess = false, isError = false) }

      userCase(email = currentEmail, password = currentPassword)
        .onSuccess {
          _uiState.update { it.copy(isLoading = false, isSuccess = true, isError = false) }
        }
        .onFailure { throwable ->
          _uiState.update {
            when (throwable) {
              is UserException.NotFound ->
                it.copy(isLoading = false, userError = AuthError.UserNotFoundError)

              is UserException.Password.Incorrect ->
                it.copy(
                  isLoading = false,
                  passwordError = AuthError.PasswordError.Incorrect
                )

              is UserException.Email.Empty ->
                it.copy(isLoading = false, emailError = AuthError.EmailError.Required)

              is UserException.Email.Invalid ->
                it.copy(isLoading = false, emailError = AuthError.EmailError.Invalid)

              is UserException.Password.Empty ->
                it.copy(
                  isLoading = false,
                  passwordError = AuthError.PasswordError.Required
                )

              is UserException.Password.Weak ->
                it.copy(isLoading = false, passwordError = AuthError.PasswordError.Weak)

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

  fun updatePassword(value: String) {
    if (_uiState.value.password == value) return
    _uiState.update {
      it.copy(
        password = value,
        passwordError = null,
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
    private const val TAG = "LoginViewModel"
  }
}
