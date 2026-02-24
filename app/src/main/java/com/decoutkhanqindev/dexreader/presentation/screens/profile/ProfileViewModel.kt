package com.decoutkhanqindev.dexreader.presentation.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.exception.AuthException
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.usecase.user.LogoutUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.user.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
  private val updateUserProfileUseCase: UpdateUserProfileUseCase,
  private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
  private val _uiState = MutableStateFlow(ProfileUiState())
  val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

  fun updateUserProfile() {
    val currentUiState = _uiState.value
    if (currentUiState.isLoading) return

    viewModelScope.launch {
      _uiState.update {
        it.copy(
          isLoading = true,
          isUpdateUserSuccess = false,
          isUpdateUserError = false,
          isLogoutUserSuccess = false,
          isLogoutUserError = false
        )
      }

      if (currentUiState.user != null) {
        updateUserProfileUseCase(
          currentUser = currentUiState.user,
          newName = currentUiState.newName,
          newProfilePicUrl = currentUiState.newProfilePictureUrl
        )
          .onSuccess {
            _uiState.update {
              it.copy(
                isLoading = false,
                isUpdateUserSuccess = true,
                isUpdateUserError = false,
                isValidName = true
              )
            }
          }
          .onFailure { throwable ->
            _uiState.update { currentState ->
              when (throwable) {
                is AuthException.Name.Empty ->
                  currentState.copy(
                    isLoading = false,
                    isUpdateUserSuccess = false,
                    isValidName = false,
                    isUpdateUserError = true
                  )

                else ->
                  currentState.copy(
                    isLoading = false,
                    isUpdateUserSuccess = false,
                    isUpdateUserError = true
                  )
              }
            }

            Log.d(TAG, "updateUserProfile has error: ${throwable.stackTraceToString()}")
          }
      } else {
        _uiState.update { it.copy(isLoading = false) }
      }
    }
  }

  fun logoutUser() {
    if (_uiState.value.isLoading) return

    viewModelScope.launch {
      _uiState.update {
        it.copy(
          isLoading = true,
          isLogoutUserSuccess = false,
          isLogoutUserError = false,
          isUpdateUserSuccess = false,
          isUpdateUserError = false,
        )
      }

      logoutUseCase()
        .onSuccess {
          _uiState.update {
            it.copy(
              isLoading = false,
              isLogoutUserSuccess = true,
              isLogoutUserError = false
            )
          }
        }
        .onFailure { throwable ->
          _uiState.update {
            it.copy(
              isLoading = false,
              isLogoutUserSuccess = false,
              isLogoutUserError = true
            )
          }

          Log.d(TAG, "logoutUser has error: ${throwable.stackTraceToString()}")
        }
    }
  }

  fun updateCurrentUser(value: User?) {
    val currentUiState = _uiState.value

    if (currentUiState.user == value &&
      currentUiState.newName == value?.name &&
      currentUiState.newProfilePictureUrl == value?.profilePictureUrl
    ) return

    _uiState.update {
      it.copy(
        isLoading = false,
        user = value,
        isUpdateUserSuccess = false,
        isUpdateUserError = false
      )
    }
  }

  fun updateUserName(value: String) {
    if (_uiState.value.newName == value) return
    _uiState.update {
      it.copy(
        isLoading = false,
        newName = value,
        isValidName = value.isNotBlank(),
        isUpdateUserSuccess = false,
        isUpdateUserError = false
      )
    }
  }

  fun updateUserPicUrl(value: String) {
    if (_uiState.value.newProfilePictureUrl == value) return
    _uiState.update {
      it.copy(
        newProfilePictureUrl = value,
        isLoading = false,
        isUpdateUserSuccess = false,
        isUpdateUserError = false
      )
    }
  }

  fun retryUpdateUserProfile() {
    if (_uiState.value.isUpdateUserError) updateUserProfile()
  }

  fun retryLogoutUser() {
    if (_uiState.value.isLogoutUserError) logoutUser()
  }

  companion object {
    private const val TAG = "ProfileViewModel"
  }
}