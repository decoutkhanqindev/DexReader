package com.decoutkhanqindev.dexreader.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.user.User
import com.decoutkhanqindev.dexreader.domain.usecase.user.profile.ObserveCurrentUserUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.user.profile.ObserveUserProfileUseCase
import com.decoutkhanqindev.dexreader.presentation.mapper.UserMapper.toUserModel
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
  private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
  private val observeUserProfileUseCase: ObserveUserProfileUseCase,
) : ViewModel() {
  private val _isUserLoggedIn = MutableStateFlow(false)
  val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

  private val _domainUserProfile = MutableStateFlow<User?>(null)
  val userProfile: StateFlow<UserModel?> = _domainUserProfile
    .map { it?.toUserModel() }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.Eagerly,
      initialValue = null
    )

  // manage observe user profile job
  private var userProfileJob: Job? = null

  init {
    observeCurrentUser()
  }

  private fun observeCurrentUser() {
    viewModelScope.launch {
      observeCurrentUserUseCase().collect { result ->
        result
          .onSuccess {
            _isUserLoggedIn.value = it != null
            if (it != null) observeUserProfile(userId = it.id)
            else {
              // user is null like user logged out
              // clear user profile state and cancel user profile job
              cancelUserProfileJob()
              _domainUserProfile.value = null
            }
          }
          .onFailure {
            _isUserLoggedIn.value = false
            Log.d(TAG, "observeCurrentUser have error: ${it.stackTraceToString()}")
          }
      }
    }
  }

  private fun observeUserProfile(userId: String) {
    // cancels any previous observation before starting a new one
    // make sure only one active job for per user
    cancelUserProfileJob()
    userProfileJob = viewModelScope.launch {
      observeUserProfileUseCase(userId).collect { result ->
        result
          .onSuccess { _domainUserProfile.value = it }
          .onFailure {
            _domainUserProfile.value = null
            Log.d(TAG, "observeUserProfile have error: ${it.stackTraceToString()}")
          }
      }
    }
  }

  private fun cancelUserProfileJob() {
    userProfileJob?.cancel()
    userProfileJob = null
  }

  override fun onCleared() {
    cancelUserProfileJob()
    super.onCleared()
  }

  companion object {
    private const val TAG = "UserViewModel"
  }
}