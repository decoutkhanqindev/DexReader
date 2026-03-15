package com.decoutkhanqindev.dexreader.presentation.screens.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.user.RegisterUseCase
import com.decoutkhanqindev.dexreader.presentation.mapper.UiErrorMapper.toUserUiError
import com.decoutkhanqindev.dexreader.presentation.model.error.UserUiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject
constructor(
  private val useCase: RegisterUseCase,
) : ViewModel() {
  private val _uiState = MutableStateFlow(RegisterUiState())
  val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

  fun submit() {
    val currentUiState = _uiState.value

    if (currentUiState.isLoading) return

    val currentEmail = currentUiState.email.trim()
    val currentPassword = currentUiState.password.trim()
    val currentConfirmPassword = currentUiState.confirmPassword.trim()
    val currentName = currentUiState.name.trim()

    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true, isSuccess = false, isError = false) }

      useCase(
        email = currentEmail,
        password = currentPassword,
        confirmPassword = currentConfirmPassword,
        name = currentName
      )
        .onSuccess {
          _uiState.update { it.copy(isLoading = false, isSuccess = true, isError = false) }
        }
        .onFailure { throwable ->
          _uiState.update {
            when (val error = throwable.toUserUiError()) {
              is UserUiError.Email -> it.copy(isLoading = false, emailError = error)
              is UserUiError.Password -> it.copy(isLoading = false, passwordError = error)
              is UserUiError.ConfirmPassword ->
                it.copy(isLoading = false, confirmPasswordError = error)

              is UserUiError.Name -> it.copy(isLoading = false, nameError = error)
              is UserUiError.RegistrationFailed ->
                it.copy(isLoading = false, isSuccess = false, isError = true)

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

  fun updateConfirmPassword(value: String) {
    if (_uiState.value.confirmPassword == value) return
    _uiState.update {
      it.copy(
        confirmPassword = value,
        confirmPasswordError = null,
        isLoading = false,
        isSuccess = false,
        isError = false
      )
    }
  }

  fun updateName(value: String) {
    if (_uiState.value.name == value) return
    _uiState.update {
      it.copy(name = value, nameError = null, isLoading = false, isSuccess = false, isError = false)
    }
  }

  fun retry() {
    if (_uiState.value.isError) submit()
  }

  companion object {
    private const val TAG = "RegisterViewModel"
  }
}
