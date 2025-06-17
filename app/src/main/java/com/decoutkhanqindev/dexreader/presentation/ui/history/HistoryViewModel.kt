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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
  private val observeHistoryUseCase: ObserveHistoryUseCase,
  private val removeFromHistoryUseCase: RemoveFromHistoryUseCase
) : ViewModel() {
  private val _historyUiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Idle)
  val historyUiState: StateFlow<HistoryUiState> = _historyUiState.asStateFlow()

  private val _removeFromHistoryUiState = MutableStateFlow(RemoveFromHistoryUiState())
  val removeFromHistoryUiState: StateFlow<RemoveFromHistoryUiState> =
    _removeFromHistoryUiState.asStateFlow()

  private val _userId = MutableStateFlow<String?>(null)

  private var observeHistoryJob: Job? = null

  init {
    observeHistoryFirstPage()
  }

  private fun observeHistoryFirstPage() {
    cancelObserveHistoryJob()
    observeHistoryJob = viewModelScope.launch {
      _historyUiState.value = HistoryUiState.FirstPageLoading

      _userId.collectLatest { userId ->
        if (userId == null) {
          _historyUiState.value = HistoryUiState.Idle
          cancelObserveHistoryJob()
          return@collectLatest
        }

        try {
          observeHistoryUseCase(
            userId = userId,
            limit = READING_HISTORY_LIST_PER_PAGE_SIZE,
          ).collect { result ->
            result
              .onSuccess { readingHistoryList ->
                val hasNextPage = readingHistoryList.size >= READING_HISTORY_LIST_PER_PAGE_SIZE
                _historyUiState.value = HistoryUiState.Content(
                  readingHistoryList = readingHistoryList,
                  currentPage = FIRST_PAGE,
                  nextPageState =
                    if (!hasNextPage) HistoryNextPageState.NO_MORE_ITEMS
                    else HistoryNextPageState.IDLE
                )
              }
              .onFailure { throwable ->
                if (throwable.message?.contains(PERMISSION_DENIED_EXCEPTION) == true && _userId.value == null) {
                  _historyUiState.value = HistoryUiState.Idle
                  return@onFailure
                }

                _historyUiState.value = HistoryUiState.FirstPageError
                Log.d(TAG, "observeHistoryFirstPage have error: ${throwable.stackTraceToString()}")
              }
          }
        } catch (e: Exception) {
          if (e.message?.contains(PERMISSION_DENIED_EXCEPTION) == true && _userId.value == null)
            _historyUiState.value = HistoryUiState.Idle
          else {
            _historyUiState.value = HistoryUiState.FirstPageError
            Log.d(TAG, "observeHistoryFirstPage have error: ${e.stackTraceToString()}")
          }
        }
      }
    }
  }

  fun observeHistoryNextPage() {
    when (val currentUiState = _historyUiState.value) {
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
      _historyUiState.value = currentUiState.copy(nextPageState = HistoryNextPageState.LOADING)

      val currentReadingHistoryList = currentUiState.readingHistoryList
      val nextPage = currentUiState.currentPage + 1
      val lastReadingHistoryId = currentReadingHistoryList.lastOrNull()?.id

      _userId.collectLatest { userId ->
        if (userId == null) {
          _historyUiState.value = HistoryUiState.Idle
          cancelObserveHistoryJob()
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
                _historyUiState.value = currentUiState.copy(
                  readingHistoryList = allReadingHistoryList,
                  currentPage = nextPage,
                  nextPageState =
                    if (!hasNextPage) HistoryNextPageState.NO_MORE_ITEMS
                    else HistoryNextPageState.IDLE
                )
              }
              .onFailure { throwable ->
                if (throwable.message?.contains(PERMISSION_DENIED_EXCEPTION) == true && _userId.value == null)
                  return@onFailure

                _historyUiState.value =
                  currentUiState.copy(nextPageState = HistoryNextPageState.ERROR)
                Log.d(
                  TAG,
                  "observeHistoryNextPageInternal have error: ${throwable.stackTraceToString()}"
                )
              }
          }
        } catch (e: Exception) {
          if (e.message?.contains(PERMISSION_DENIED_EXCEPTION) == true && _userId.value == null)
            return@collectLatest
          else {
            _historyUiState.value = currentUiState.copy(nextPageState = HistoryNextPageState.ERROR)
            Log.d(TAG, "observeHistoryNextPageInternal setup error: ${e.stackTraceToString()}")
          }
        }
      }
    }
  }

  fun removeFromHistory() {
    val currentRemoveFromHistoryUiState = _removeFromHistoryUiState.value
    if (currentRemoveFromHistoryUiState.isLoading ||
      currentRemoveFromHistoryUiState.readingHistoryId == null ||
      _historyUiState.value !is HistoryUiState.Content
    ) return

    viewModelScope.launch {
      _removeFromHistoryUiState.update {
        it.copy(
          isLoading = true,
          isSuccess = false,
          isError = false
        )
      }

      val readingHistoryId = currentRemoveFromHistoryUiState.readingHistoryId

      _userId.value?.let { userId ->
        removeFromHistoryUseCase(
          userId = userId,
          readingHistoryId = readingHistoryId
        )
          .onSuccess {
            _removeFromHistoryUiState.update {
              it.copy(
                isLoading = false,
                readingHistoryId = null,
                isSuccess = true,
                isError = false
              )
            }
          }
          .onFailure {
            _removeFromHistoryUiState.update {
              it.copy(
                isLoading = false,
                isSuccess = false,
                isError = true
              )
            }
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
    if (_removeFromHistoryUiState.value.readingHistoryId == readingHistoryId) return
    _removeFromHistoryUiState.update {
      it.copy(
        isLoading = false,
        readingHistoryId = readingHistoryId,
        isSuccess = false,
        isError = false
      )
    }
  }

  fun retryObserveHistoryFirstPage() {
    if (_historyUiState.value is HistoryUiState.FirstPageError)
      observeHistoryFirstPage()
  }

  fun retryObserveHistoryNextPage() {
    val currentUiState = _historyUiState.value
    if (currentUiState is HistoryUiState.Content &&
      currentUiState.nextPageState == HistoryNextPageState.ERROR
    ) observeHistoryNextPageInternal(currentUiState)
  }

  fun retryRemoveFromHistory() {
    if (_removeFromHistoryUiState.value.isError) removeFromHistory()
  }

  fun reset() {
    _historyUiState.value = HistoryUiState.Idle
    _userId.value = null
    _removeFromHistoryUiState.value = RemoveFromHistoryUiState()
  }

  private fun cancelObserveHistoryJob() {
    observeHistoryJob?.cancel()
    observeHistoryJob = null
  }

  override fun onCleared() {
    cancelObserveHistoryJob()
    super.onCleared()
  }

  companion object {
    private const val TAG = "HistoryViewModel"
    private const val FIRST_PAGE = 1
    private const val READING_HISTORY_LIST_PER_PAGE_SIZE = 10
    private const val PERMISSION_DENIED_EXCEPTION = "PERMISSION_DENIED"
  }
}