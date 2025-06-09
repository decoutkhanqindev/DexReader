package com.decoutkhanqindev.dexreader.presentation.ui.auth.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.user.LoginUserUseCase
import com.decoutkhanqindev.dexreader.presentation.ui.auth.AuthError
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val loginUserUserCase: LoginUserUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState())
  val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

  fun loginUser() {
    viewModelScope.launch {
      val currentUiState = _uiState.value
      if (currentUiState.isLoading) return@launch

      _uiState.update { it.copy(isLoading = true, isError = false) }

      if (!currentUiState.isValidEmail || !currentUiState.isValidPassword) {
        _uiState.update {
          it.copy(
            isLoading = false,
            isSuccess = false,
          )
        }
        return@launch
      }

      val loginUserResult = loginUserUserCase(
        email = currentUiState.email.trim(),
        password = currentUiState.password.trim()
      )
      loginUserResult
        .onSuccess {
          _uiState.update {
            it.copy(
              isLoading = false,
              isSuccess = true
            )
          }
        }
        .onFailure { throwable ->
          _uiState.update {
            when (throwable) {
              is FirebaseAuthInvalidUserException -> {
                it.copy(
                  isLoading = false,
                  isSuccess = false,
                  userError = AuthError.UserNotFoundError
                )
              }

              is FirebaseAuthInvalidCredentialsException -> {
                it.copy(
                  isLoading = false,
                  isSuccess = false,
                  isValidPassword = false,
                  passwordError = AuthError.PasswordError.Incorrect
                )
              }

              else -> {
                currentUiState.copy(
                  isLoading = false,
                  isSuccess = false,
                  isError = true
                )
              }
            }
          }
          Log.d(TAG, "loginUser have error: ${throwable.stackTraceToString()}")
        }
    }
  }

  fun updateEmailField(email: String) {
    _uiState.update {
      it.copy(
        email = email,
        emailError = when {
          email.isBlank() -> AuthError.EmailError.Required
          !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> AuthError.EmailError.Invalid
          else -> AuthError.UnknownError
        },
        isValidEmail = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches(),
        isError = false
      )
    }
  }

  fun updatePasswordField(password: String) {
    _uiState.update {
      it.copy(
        password = password,
        passwordError = when {
          password.isBlank() -> AuthError.PasswordError.Required
          password.length < MIN_LENGTH_PASSWORD -> AuthError.PasswordError.Weak
          else -> AuthError.UnknownError
        },
        isValidPassword = password.isNotBlank() && password.length >= MIN_LENGTH_PASSWORD,
        isError = false
      )
    }
  }

  fun reset() {
    _uiState.update { LoginUiState() }
  }

  companion object {
    private const val TAG = "LoginViewModel"
    private const val MIN_LENGTH_PASSWORD = 8
  }
}