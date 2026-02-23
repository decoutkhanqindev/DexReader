package com.decoutkhanqindev.dexreader.presentation.screens.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.exception.AuthException
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.usecase.user.AddAndUpdateUserProfileUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.user.RegisterUseCase
import com.decoutkhanqindev.dexreader.presentation.screens.auth.AuthError
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
  private val registerUseCase: RegisterUseCase,
  private val addUserProfileUseCase: AddAndUpdateUserProfileUseCase,
) : ViewModel() {
  private val _uiState = MutableStateFlow(RegisterUiState())
  val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

  fun submit() {
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

      // Local UI validation for fields not validated by RegisterUseCase
      if (currentUiState.password != currentUiState.confirmPassword) {
        _uiState.update {
          it.copy(
            isLoading = false,
            isValidConfirmPassword = false,
            confirmPasswordError = AuthError.ConfirmPasswordError.DoesNotMatch
          )
        }
        return@launch
      }

      if (currentUiState.name.isBlank()) {
        _uiState.update {
          it.copy(
            isLoading = false,
            isValidName = false,
            nameError = AuthError.NameError.Required
          )
        }
        return@launch
      }

      registerUseCase(
        email = currentUiState.email.trim(),
        password = currentUiState.password.trim()
      )
        .onSuccess { user ->
          val newUser =
            User(
              id = user.id,
              name = currentUiState.name.trim(),
              email = currentUiState.email.trim(),
              profilePictureUrl = null
            )
          addUserProfile(user = newUser)
        }
        .onFailure { throwable ->
          _uiState.update { currentState ->
            when (throwable) {
              is FirebaseAuthUserCollisionException -> {
                currentState.copy(
                  isLoading = false,
                  isSuccess = false,
                  isValidEmail = false,
                  emailError = AuthError.EmailError.AlreadyInUse
                )
              }

              is AuthException.InvalidEmail -> {
                currentState.copy(
                  isLoading = false,
                  isSuccess = false,
                  isValidEmail = false,
                  emailError = AuthError.EmailError.Invalid
                )
              }

              is AuthException.WeakPassword -> {
                currentState.copy(
                  isLoading = false,
                  isSuccess = false,
                  isValidPassword = false,
                  passwordError = AuthError.PasswordError.Weak
                )
              }

              else -> {
                currentState.copy(
                  isLoading = false,
                  isSuccess = false,
                  isError = true
                )
              }
            }
          }

          Log.d(TAG, "submit has error: ${throwable.stackTraceToString()}")
        }
    }
  }

  private fun addUserProfile(user: User) {
    viewModelScope.launch {
      addUserProfileUseCase(user)
        .onSuccess {
          _uiState.update {
            it.copy(
              isLoading = false,
              isSuccess = true,
              isError = false
            )
          }
        }
        .onFailure { throwable ->
          _uiState.update {
            it.copy(
              isLoading = false,
              isSuccess = false,
              isError = true,
            )
          }

          Log.e(TAG, "addUserProfile has error: ${throwable.stackTraceToString()}")
        }
    }
  }

  fun updateEmail(value: String) {
    if (_uiState.value.email == value) return
    _uiState.update {
      it.copy(
        email = value,
        emailError = AuthError.UnknownError,
        isValidEmail = true,
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
        passwordError = AuthError.UnknownError,
        isValidPassword = true,
        isLoading = false,
        isSuccess = false,
        isError = false
      )
    }
  }

  fun updateConfirmPassword(value: String) {
    if (_uiState.value.confirmPassword == value) return
    _uiState.update { currentState ->
      currentState.copy(
        confirmPassword = value,
        confirmPasswordError = AuthError.UnknownError,
        isValidConfirmPassword = true,
        isLoading = false,
        isSuccess = false,
        isError = false
      )
    }
  }

  fun updateName(value: String) {
    if (_uiState.value.name == value) return
    _uiState.update {
      it.copy(
        name = value,
        nameError = AuthError.UnknownError,
        isValidName = true,
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
    private const val TAG = "RegisterViewModel"
  }
}
