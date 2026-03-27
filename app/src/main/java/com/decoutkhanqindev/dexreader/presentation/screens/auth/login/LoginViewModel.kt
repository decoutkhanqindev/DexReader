package com.decoutkhanqindev.dexreader.presentation.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.user.LoginUseCase
import com.decoutkhanqindev.dexreader.presentation.error.UserError
import com.decoutkhanqindev.dexreader.presentation.mapper.ErrorMapper.toUserError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.util.Log
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val useCase: LoginUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(LoginUiState())
  val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

  fun submit() {
    val currentUiState = _uiState.value

    if (currentUiState.isLoading) return

    val currentEmail = currentUiState.email.trim()
    val currentPassword = currentUiState.password.trim()

    viewModelScope.launch {
      _uiState.update {
        it.copy(
          isLoading = true,
          isSuccess = false,
          isError = false
        )
      }

      useCase(email = currentEmail, password = currentPassword)
        .onSuccess {
          _uiState.update {
            it.copy(
              isLoading = false,
              isSuccess = true,
              isError = false,
              password = ""
            )
          }
        }
        .onFailure { throwable ->
          _uiState.update {
            when (val error = throwable.toUserError()) {
              is UserError.NotFound -> it.copy(
                isLoading = false,
                isSuccess = false,
                isError = true
              )

              is UserError.Email -> it.copy(
                isLoading = false,
                emailError = error
              )

              is UserError.Password -> it.copy(
                isLoading = false,
                passwordError = error
              )

              else -> it.copy(
                isLoading = false,
                isSuccess = false,
                isError = true
              )
            }
          }

          Timber.tag(TAG).d("submit has error: ${throwable.stackTraceToString()}")
        }
    }
  }

  fun updateEmail(value: String) {
    if (_uiState.value.email == value) return
    _uiState.update { it.copy(email = value, emailError = null) }
  }

  fun updatePassword(value: String) {
    if (_uiState.value.password == value) return
    _uiState.update { it.copy(password = value, passwordError = null) }
  }

  fun dismissError() {
    _uiState.update { it.copy(isError = false) }
  }

  fun dismissSuccess() {
    _uiState.update { it.copy(isSuccess = false) }
  }

  fun retry() {
    if (_uiState.value.isError) submit()
  }

  companion object {
    private const val TAG = "LoginViewModel"
  }
}
