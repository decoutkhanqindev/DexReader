package com.decoutkhanqindev.dexreader.presentation.screens.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.domain.usecase.history.ObserveHistoryUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.history.RemoveFromHistoryUseCase
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BasePaginationUiState
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
  private val _historyUiState =
    MutableStateFlow<BasePaginationUiState<ReadingHistory>>(BasePaginationUiState.FirstPageLoading)
  val historyUiState: StateFlow<BasePaginationUiState<ReadingHistory>> =
    _historyUiState.asStateFlow()

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
      _historyUiState.value = BasePaginationUiState.FirstPageLoading

      _userId.collectLatest { userId ->
        if (userId == null) {
          _historyUiState.value = BasePaginationUiState.FirstPageLoading
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
                _historyUiState.value = BasePaginationUiState.Content(
                  currentList = readingHistoryList,
                  currentPage = FIRST_PAGE,
                  nextPageState =
                    if (!hasNextPage) BaseNextPageState.NO_MORE_ITEMS
                    else BaseNextPageState.IDLE
                )
              }
              .onFailure { throwable ->
                if (throwable.message?.contains(PERMISSION_DENIED_EXCEPTION) == true && _userId.value == null) {
                  _historyUiState.value = BasePaginationUiState.FirstPageLoading
                  return@onFailure
                }

                _historyUiState.value = BasePaginationUiState.FirstPageError
                Log.d(TAG, "observeHistoryFirstPage have error: ${throwable.stackTraceToString()}")
              }
          }
        } catch (e: Exception) {
          if (e.message?.contains(PERMISSION_DENIED_EXCEPTION) == true && _userId.value == null)
            _historyUiState.value = BasePaginationUiState.FirstPageLoading
          else {
            _historyUiState.value = BasePaginationUiState.FirstPageError
            Log.d(TAG, "observeHistoryFirstPage have error: ${e.stackTraceToString()}")
          }
        }
      }
    }
  }

  fun observeHistoryNextPage() {
    when (val currentUiState = _historyUiState.value) {
      BasePaginationUiState.FirstPageError,
      BasePaginationUiState.FirstPageLoading,
        -> return

      is BasePaginationUiState.Content -> {
        when (currentUiState.nextPageState) {
          BaseNextPageState.LOADING,
          BaseNextPageState.NO_MORE_ITEMS
            -> return

          BaseNextPageState.ERROR -> retryObserveHistoryNextPage()
          BaseNextPageState.IDLE -> observeHistoryNextPageInternal(currentUiState)
        }
      }
    }
  }

  private fun observeHistoryNextPageInternal(currentUiState: BasePaginationUiState.Content<ReadingHistory>) {
    observeHistoryJob = viewModelScope.launch {
      _historyUiState.value = currentUiState.copy(nextPageState = BaseNextPageState.LOADING)

      val currentReadingHistoryList = currentUiState.currentList
      val nextPage = currentUiState.currentPage + 1
      val lastReadingHistoryId = currentReadingHistoryList.lastOrNull()?.id

      _userId.collectLatest { userId ->
        if (userId == null) {
          _historyUiState.value = BasePaginationUiState.FirstPageLoading
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
                  currentList = allReadingHistoryList,
                  currentPage = nextPage,
                  nextPageState =
                    if (!hasNextPage) BaseNextPageState.NO_MORE_ITEMS
                    else BaseNextPageState.IDLE
                )
              }
              .onFailure { throwable ->
                if (throwable.message?.contains(PERMISSION_DENIED_EXCEPTION) == true && _userId.value == null)
                  return@onFailure

                _historyUiState.value =
                  currentUiState.copy(nextPageState = BaseNextPageState.ERROR)
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
            _historyUiState.value = currentUiState.copy(nextPageState = BaseNextPageState.ERROR)
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
      _historyUiState.value !is BasePaginationUiState.Content
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
          .onFailure { throwable ->
            _removeFromHistoryUiState.update {
              it.copy(
                isLoading = false,
                isSuccess = false,
                isError = true
              )
            }
            Log.d(TAG, "removeFromHistory have error: ${throwable.stackTraceToString()}")
          }
      }
    }
  }

  fun updateUserId(userId: String?) {
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
    if (_historyUiState.value is BasePaginationUiState.FirstPageError)
      observeHistoryFirstPage()
  }

  fun retryObserveHistoryNextPage() {
    val currentUiState = _historyUiState.value
    if (currentUiState is BasePaginationUiState.Content &&
      currentUiState.nextPageState == BaseNextPageState.ERROR
    ) observeHistoryNextPageInternal(currentUiState)
  }

  fun retryRemoveFromHistory() {
    if (_removeFromHistoryUiState.value.isError) removeFromHistory()
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