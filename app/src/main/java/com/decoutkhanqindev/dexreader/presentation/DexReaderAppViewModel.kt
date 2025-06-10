package com.decoutkhanqindev.dexreader.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.usecase.user.ObserveCurrentUserUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.user.ObserveUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DexReaderAppViewModel @Inject constructor(
  private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
  private val observeUserProfileUseCase: ObserveUserProfileUseCase
) : ViewModel() {
  private val _isUserLoggedIn = MutableStateFlow(false)
  val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

  private val _userProfile = MutableStateFlow<User?>(null)
  val userProfile: StateFlow<User?> = _userProfile.asStateFlow()

  // manage observe user profile job
  private var userProfileJob: Job? = null

  init {
    observeCurrentUser()
  }

  private fun observeCurrentUser() {
    viewModelScope.launch {
      observeCurrentUserUseCase().collect { userResult ->
        userResult
          .onSuccess {
            _isUserLoggedIn.value = it != null
            if (it != null) {
              observeUserProfile(userId = it.id)
            } else {
              // user is null like user logged out
              // clear user profile state and cancel user profile job
              userProfileJob?.cancel()
              _userProfile.value = null
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
    userProfileJob?.cancel()
    userProfileJob = viewModelScope.launch {
      observeUserProfileUseCase(userId).collect { userProfileResult ->
        userProfileResult
          .onSuccess { _userProfile.value = it }
          .onFailure {
            _userProfile.value = null
            Log.d(TAG, "observeUserProfile have error: ${it.stackTraceToString()}")
          }
      }
    }
  }

  companion object {
    private const val TAG = "DexReaderAppViewModel"
  }
}