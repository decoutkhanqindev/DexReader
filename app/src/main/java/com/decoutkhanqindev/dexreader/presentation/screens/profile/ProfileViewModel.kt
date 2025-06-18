package com.decoutkhanqindev.dexreader.presentation.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.usecase.user.AddAndUpdateUserProfileUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.user.LogoutUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
  private val updateUserProfileUseCase: AddAndUpdateUserProfileUseCase,
  private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {
  private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState())
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

      if (currentUiState.updatedName.isNullOrBlank()) {
        _uiState.update {
          it.copy(
            isLoading = false,
            isUpdateUserSuccess = false,
            isValidName = false,
            isUpdateUserError = true
          )
        }
        return@launch
      }

      if (currentUiState.user != null) {
        val hasNameChanged = currentUiState.user.name != currentUiState.updatedName
        val hasPicChanged =
          currentUiState.user.profilePictureUrl != currentUiState.updatedProfilePictureUrl
        if (!hasNameChanged && !hasPicChanged) {
          _uiState.update {
            it.copy(
              isLoading = false,
              isUpdateUserError = false,
              isUpdateUserSuccess = true,
            )
          }
          return@launch
        }

        val updatedUser = currentUiState.user.copy(
          name = currentUiState.updatedName,
          profilePictureUrl = currentUiState.updatedProfilePictureUrl
        )

        updateUserProfileUseCase(user = updatedUser)
          .onSuccess {
            _uiState.update {
              it.copy(
                isLoading = false,
                isUpdateUserSuccess = true,
                isUpdateUserError = false
              )
            }
          }
          .onFailure {
            _uiState.update {
              it.copy(
                isLoading = false,
                isUpdateUserSuccess = false,
                isUpdateUserError = true
              )
            }
            Log.d(TAG, "updateUserProfile have error: ${it.stackTraceToString()}")
          }
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

      logoutUserUseCase()
        .onSuccess {
          _uiState.update {
            it.copy(
              isLoading = false,
              isLogoutUserSuccess = true,
              isLogoutUserError = false
            )
          }
        }
        .onFailure {
          _uiState.update {
            it.copy(
              isLoading = false,
              isLogoutUserSuccess = false,
              isLogoutUserError = true
            )
          }
          Log.d(TAG, "logoutUser have error: ${it.stackTraceToString()}")
        }
    }
  }

  fun updateCurrentUser(user: User?) {
    val currentUiState = _uiState.value
    if (currentUiState.user == user &&
      currentUiState.updatedName == user?.name &&
      currentUiState.updatedProfilePictureUrl == user?.profilePictureUrl
    ) return
    _uiState.update {
      it.copy(
        isLoading = false,
        user = user,
        isUpdateUserSuccess = false,
        isUpdateUserError = false
      )
    }
  }

  fun updateUserName(name: String) {
    if (_uiState.value.updatedName == name) return
    _uiState.update {
      it.copy(
        isLoading = false,
        updatedName = name,
        isValidName = name.isNotBlank(),
        isUpdateUserSuccess = false,
        isUpdateUserError = false
      )
    }
  }

  fun updateUserPicUrl(url: String) {
    if (_uiState.value.updatedProfilePictureUrl == url) return
    _uiState.update {
      it.copy(
        updatedProfilePictureUrl = url,
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