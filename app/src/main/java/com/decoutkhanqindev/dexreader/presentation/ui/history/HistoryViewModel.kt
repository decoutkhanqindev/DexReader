package com.decoutkhanqindev.dexreader.presentation.ui.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.history.ObserveHistoryUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.history.RemoveFromHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
  private val observeHistoryUseCase: ObserveHistoryUseCase,
  private val removeFromHistoryUseCase: RemoveFromHistoryUseCase
) : ViewModel() {
  private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Idle)
  val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

  private val _userId = MutableStateFlow<String?>(null)
  private val userId: StateFlow<String?> = _userId.asStateFlow()

  private val _removeReadingHistoryId = MutableStateFlow<String?>(null)
  private val removeReadingHistoryId: StateFlow<String?> = _removeReadingHistoryId.asStateFlow()

  private var observeHistoryJob: Job? = null

  init {
    observeHistoryFirstPage()
  }

  private fun observeHistoryFirstPage() {
    cancelObserveHistoryJob()
    observeHistoryJob = viewModelScope.launch {
      _uiState.value = HistoryUiState.FirstPageLoading

      userId.collectLatest { userId ->
        if (userId == null) {
          _uiState.value = HistoryUiState.Idle
          return@collectLatest
        }

        try {
          observeHistoryUseCase(
            userId = userId,
            limit = READING_HISTORY_LIST_PER_PAGE_SIZE,
            lastReadingHistoryId = null
          ).collect { result ->
            result
              .onSuccess { readingHistoryList ->
                val hasNextPage = readingHistoryList.size >= READING_HISTORY_LIST_PER_PAGE_SIZE
                _uiState.value = HistoryUiState.Content(
                  readingHistoryList = readingHistoryList,
                  currentPage = FIRST_PAGE,
                  nextPageState =
                    if (!hasNextPage) HistoryNextPageState.NO_MORE_ITEMS
                    else HistoryNextPageState.IDLE
                )
              }
              .onFailure { throwable ->
                if (throwable.message?.contains(PERMISSION_DENIED_EXCEPTION) == true &&
                  _userId.value == null
                ) {
                  _uiState.value = HistoryUiState.Idle
                  return@onFailure
                }

                _uiState.value = HistoryUiState.FirstPageError
                Log.d(TAG, "observeHistoryFirstPage have error: ${throwable.stackTraceToString()}")
              }
          }
        } catch (e: Exception) {
          if (e.message?.contains(PERMISSION_DENIED_EXCEPTION) == true &&
            _userId.value == null
          ) {
            _uiState.value = HistoryUiState.Idle
          } else {
            _uiState.value = HistoryUiState.FirstPageError
            Log.d(TAG, "observeHistoryFirstPage have error: ${e.stackTraceToString()}")
          }
        }
      }
    }
  }

  fun observeHistoryNextPage() {
    when (val currentUiState = _uiState.value) {
      HistoryUiState.Idle,
      HistoryUiState.FirstPageError,
      HistoryUiState.FirstPageLoading,
        -> return

      is HistoryUiState.Content -> {
        when (currentUiState.nextPageState) {
          HistoryNextPageState.LOADING,
          HistoryNextPageState.NO_MORE_ITEMS
            -> return

          HistoryNextPageState.ERROR -> retryObserveHistoryNextPage()
          HistoryNextPageState.IDLE -> observeHistoryNextPageInternal(currentUiState)
        }
      }
    }
  }

  private fun observeHistoryNextPageInternal(currentUiState: HistoryUiState.Content) {
    observeHistoryJob = viewModelScope.launch {
      _uiState.value = currentUiState.copy(nextPageState = HistoryNextPageState.LOADING)

      val currentReadingHistoryList = currentUiState.readingHistoryList
      val nextPage = currentUiState.currentPage + 1
      val lastReadingHistoryId = currentReadingHistoryList.lastOrNull()?.id

      userId.collectLatest { userId ->
        if (userId == null) {
          _uiState.value = HistoryUiState.Idle
          return@collectLatest
        }

        try {
          observeHistoryUseCase(
            userId = userId,
            limit = READING_HISTORY_LIST_PER_PAGE_SIZE,
            lastReadingHistoryId = lastReadingHistoryId
          ).collect { result ->
            result
              .onSuccess { readingHistoryList ->
                val allReadingHistoryList = currentReadingHistoryList + readingHistoryList
                val hasNextPage = readingHistoryList.size >= READING_HISTORY_LIST_PER_PAGE_SIZE
                _uiState.value = currentUiState.copy(
                  readingHistoryList = allReadingHistoryList,
                  currentPage = nextPage,
                  nextPageState =
                    if (!hasNextPage) HistoryNextPageState.NO_MORE_ITEMS
                    else HistoryNextPageState.IDLE
                )
              }
              .onFailure { throwable ->
                if (throwable.message?.contains(PERMISSION_DENIED_EXCEPTION) == true && _userId.value == null) {
                  return@onFailure
                }

                _uiState.value =
                  currentUiState.copy(nextPageState = HistoryNextPageState.ERROR)
                Log.d(
                  TAG,
                  "observeHistoryNextPageInternal have error: ${throwable.stackTraceToString()}"
                )
              }
          }
        } catch (e: Exception) {
          if (e.message?.contains(PERMISSION_DENIED_EXCEPTION) == true && _userId.value == null) {
            return@collectLatest
          } else {
            _uiState.value = currentUiState.copy(nextPageState = HistoryNextPageState.ERROR)
            Log.d(
              TAG,
              "observeHistoryNextPageInternal setup error: ${e.stackTraceToString()}"
            )
          }
        }
      }
    }
  }

  fun removeFromHistory() {
    if (_uiState.value !is HistoryUiState.Content ||
      _removeReadingHistoryId.value == null
    ) return

    viewModelScope.launch {
      userId.value?.let { userId ->
        val removeFromHistoryResult = removeFromHistoryUseCase(
          userId = userId,
          readingHistoryId = removeReadingHistoryId.value!!
        )
        removeFromHistoryResult
          .onSuccess { Log.d(TAG, "removeFromHistory success") }
          .onFailure {
            Log.d(TAG, "removeFromHistory have error: ${it.stackTraceToString()}")
          }
      }
    }
  }

  fun updateUserId(userId: String) {
    if (_userId.value == userId) return
    _userId.value = userId
  }

  fun updateRemoveReadingHistoryId(readingHistoryId: String) {
    if (_removeReadingHistoryId.value == readingHistoryId) return
    _removeReadingHistoryId.value = readingHistoryId
  }

  fun retryObserveHistoryFirstPage() {
    if (_uiState.value is HistoryUiState.FirstPageError)
      observeHistoryFirstPage()
  }

  fun retryObserveHistoryNextPage() {
    val currentUiState = _uiState.value
    if (currentUiState is HistoryUiState.Content &&
      currentUiState.nextPageState == HistoryNextPageState.ERROR
    ) observeHistoryNextPageInternal(currentUiState)
  }

  fun retryRemoveFromHistory() {
  }

  fun reset() {
    _uiState.value = HistoryUiState.Idle
    _userId.value = null
    _removeReadingHistoryId.value = null
  }

  private fun cancelObserveHistoryJob() {
    observeHistoryJob?.cancel()
    observeHistoryJob = null
  }

  override fun onCleared() {
    super.onCleared()
    cancelObserveHistoryJob()
  }

  companion object {
    private const val TAG = "HistoryViewModel"
    private const val FIRST_PAGE = 1
    private const val READING_HISTORY_LIST_PER_PAGE_SIZE = 10
    private const val PERMISSION_DENIED_EXCEPTION = "PERMISSION_DENIED"
  }
}