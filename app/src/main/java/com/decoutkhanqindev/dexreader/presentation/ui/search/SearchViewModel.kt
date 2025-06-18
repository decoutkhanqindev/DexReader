package com.decoutkhanqindev.dexreader.presentation.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.usecase.manga.SearchMangaUseCase
import com.decoutkhanqindev.dexreader.presentation.ui.common.base.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.ui.common.base.BasePaginationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val searchMangaUseCase: SearchMangaUseCase
) : ViewModel() {
  private val _suggestionsUiState =
    MutableStateFlow<SuggestionsUiState>(SuggestionsUiState.Loading)
  val suggestionsUiState: StateFlow<SuggestionsUiState> = _suggestionsUiState.asStateFlow()

  private val _resultsUiState =
    MutableStateFlow<BasePaginationUiState<Manga>>(BasePaginationUiState.FirstPageLoading)
  val resultsUiState: StateFlow<BasePaginationUiState<Manga>> = _resultsUiState.asStateFlow()

  private val _query = MutableStateFlow("")
  val query: StateFlow<String> = _query.asStateFlow()

  @OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class
  )
  val suggestionList = query
    .filter { it.isNotEmpty() }
    .debounce(DEBOUNCE_TIME_MILLIS)
    .distinctUntilChanged()
    .flatMapLatest { query ->
      flow {
        _suggestionsUiState.value = SuggestionsUiState.Loading

        searchMangaUseCase(query)
          .onSuccess { mangaList ->
            val titleList =
              mangaList.map { manga -> manga.title }.take(TAKE_SUGGESTION_LIST_SIZE)
            _suggestionsUiState.value = SuggestionsUiState.Success
            emit(titleList)
          }
          .onFailure {
            _suggestionsUiState.value = SuggestionsUiState.Error
            emit(emptyList())
            Log.d(TAG, "suggestionList have error: ${it.stackTraceToString()}")
          }
      }
    }
    .stateIn(
      scope = viewModelScope,
      started = WhileSubscribed(WHILE_SUBSCRIBED_STOP_TIMEOUT_MILLIS),
      initialValue = emptyList()
    )

  fun fetchMangaListFirstPage() {
    viewModelScope.launch {
      _resultsUiState.value = BasePaginationUiState.FirstPageLoading

      searchMangaUseCase(query.value)
        .onSuccess { mangaList ->
          val hasNextPage = mangaList.size >= MANGA_LIST_PER_PAGE_SIZE
          _resultsUiState.value = BasePaginationUiState.Content(
            currentList = mangaList,
            currentPage = FIRST_PAGE,
            nextPageState =
              if (!hasNextPage) BaseNextPageState.NO_MORE_ITEMS
              else BaseNextPageState.IDLE
          )
        }
        .onFailure {
          _resultsUiState.value = BasePaginationUiState.FirstPageError
          Log.d(
            TAG,
            "fetchMangaListFirstPage have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  fun fetchMangaListNextPage() {
    when (val currentResultsUiState = _resultsUiState.value) {
      BasePaginationUiState.FirstPageLoading,
      BasePaginationUiState.FirstPageError,
        -> return

      is BasePaginationUiState.Content -> {
        when (currentResultsUiState.nextPageState) {
          BaseNextPageState.LOADING,
          BaseNextPageState.NO_MORE_ITEMS
            -> return

          BaseNextPageState.ERROR -> retryFetchMangaListNextPage()

          BaseNextPageState.IDLE -> fetchMangaListNextPageInternal(currentResultsUiState)
        }
      }
    }
  }

  private fun fetchMangaListNextPageInternal(currentResultsUiState: BasePaginationUiState.Content<Manga>) {
    viewModelScope.launch {
      _resultsUiState.value =
        currentResultsUiState.copy(nextPageState = BaseNextPageState.LOADING)

      val currentMangaList = currentResultsUiState.currentList
      val nextPage = currentResultsUiState.currentPage + 1

      searchMangaUseCase(
        query = query.value,
        offset = currentMangaList.size
      )
        .onSuccess { nextMangaList ->
          val allMangaList = currentMangaList + nextMangaList
          val hasNextPage = nextMangaList.size >= MANGA_LIST_PER_PAGE_SIZE
          _resultsUiState.value = currentResultsUiState.copy(
            currentList = allMangaList,
            currentPage = nextPage,
            nextPageState =
              if (!hasNextPage) BaseNextPageState.NO_MORE_ITEMS
              else BaseNextPageState.IDLE
          )
        }
        .onFailure {
          _resultsUiState.value =
            currentResultsUiState.copy(nextPageState = BaseNextPageState.ERROR)
          Log.d(TAG, "fetchMangaListNextPageInternal have error: ${it.stackTraceToString()}")
        }
    }
  }

  fun updateQuery(newQuery: String) {
    if (_query.value == newQuery) return
    _query.value = newQuery
  }

  fun retry() {
    if (_resultsUiState.value is BasePaginationUiState.FirstPageError)
      fetchMangaListFirstPage()
  }

  fun retryFetchMangaListNextPage() {
    val currentResultsUiState = _resultsUiState.value
    if (currentResultsUiState is BasePaginationUiState.Content &&
      currentResultsUiState.nextPageState == BaseNextPageState.ERROR
    ) fetchMangaListNextPageInternal(currentResultsUiState)
  }

  companion object {
    private const val TAG = "SearchViewModel"
    private const val TAKE_SUGGESTION_LIST_SIZE = 10
    private const val FIRST_PAGE = 1
    private const val MANGA_LIST_PER_PAGE_SIZE = 20
    private const val DEBOUNCE_TIME_MILLIS = 500L
    private const val WHILE_SUBSCRIBED_STOP_TIMEOUT_MILLIS = 5000L
  }
}