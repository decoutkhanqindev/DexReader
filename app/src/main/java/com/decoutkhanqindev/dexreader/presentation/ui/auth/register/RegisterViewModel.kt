package com.decoutkhanqindev.dexreader.presentation.ui.auth.register

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.usecase.user.AddUserProfileUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.user.RegisterUserUseCase
import com.decoutkhanqindev.dexreader.presentation.ui.auth.AuthError
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
  private val registerUserUseCase: RegisterUserUseCase,
  private val addUserProfileUseCase: AddUserProfileUseCase
) : ViewModel() {
  private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState())
  val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

  fun registerUser() {
    viewModelScope.launch {
      val currentUiState = _uiState.value
      if (currentUiState.isLoading) return@launch

      _uiState.update { it.copy(isLoading = true) }

      if (
        !currentUiState.isValidEmail ||
        !currentUiState.isValidPassword ||
        !currentUiState.isValidConfirmPassword ||
        !currentUiState.isValidName
      ) {
        _uiState.update {
          it.copy(
            isLoading = false,
            isSuccess = false,
          )
        }
        return@launch
      }

      val registerResult = registerUserUseCase(
        email = currentUiState.email,
        password = currentUiState.password
      )
      registerResult
        .onSuccess { user ->
          val newUser = User(
            id = user.id,
            name = currentUiState.name,
            email = currentUiState.email,
            profilePictureUrl = null
          )
          addUserProfile(newUser)
        }
        .onFailure { throwable ->
          _uiState.update { currentUiState ->
            if (throwable is FirebaseAuthUserCollisionException) {
              currentUiState.copy(
                isLoading = false,
                isSuccess = false,
                isValidEmail = false,
                emailError = AuthError.EmailError.AlreadyInUse
              )
            } else {
              currentUiState.copy(
                isLoading = false,
                isSuccess = false,
              )
            }
          }
          Log.d(TAG, "registerUser have error: ${throwable.stackTraceToString()}")
        }
    }
  }

  private fun addUserProfile(user: User) {
    viewModelScope.launch {
      val result = addUserProfileUseCase(user)
      result
        .onSuccess { addedUser ->
          _uiState.update {
            it.copy(
              isLoading = false,
              isSuccess = true
            )
          }
        }
        .onFailure { throwable ->
          _uiState.update {
            it.copy(
              isLoading = false,
              isSuccess = false,
            )
          }
          Log.e(TAG, "addUserProfile have error: ${throwable.stackTraceToString()}")
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
        isValidEmail = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
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
        isValidPassword = password.isNotBlank() && password.length >= MIN_LENGTH_PASSWORD
      )
    }
  }

  fun updateConfirmPasswordField(confirmPassword: String) {
    _uiState.update { currentState ->
      currentState.copy(
        confirmPassword = confirmPassword,
        confirmPasswordError = when {
          confirmPassword.isBlank() -> AuthError.ConfirmPasswordError.Required
          confirmPassword != currentState.password -> AuthError.ConfirmPasswordError.DoesNotMatch
          else -> AuthError.UnknownError
        },
        isValidConfirmPassword = confirmPassword.isNotBlank() && confirmPassword == currentState.password
      )
    }
  }

  fun updateNameField(name: String) {
    _uiState.update {
      it.copy(
        name = name,
        nameError =
          if (name.isBlank()) AuthError.NameError.Required
          else AuthError.UnknownError,
        isValidName = name.isNotBlank()
      )
    }
  }

  fun retry() {
    registerUser()
  }

  companion object {
    private const val TAG = "RegisterViewModel"
    private const val MIN_LENGTH_PASSWORD = 8
  }
}