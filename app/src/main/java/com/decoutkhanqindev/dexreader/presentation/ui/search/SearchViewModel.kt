package com.decoutkhanqindev.dexreader.presentation.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.manga.SearchMangaUseCase
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
    MutableStateFlow<ResultsUiState>(ResultsUiState.FirstPageLoading)
  val resultsUiState: StateFlow<ResultsUiState> = _resultsUiState.asStateFlow()

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

        val mangaListResult = searchMangaUseCase(query)
        mangaListResult
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
      _resultsUiState.value = ResultsUiState.FirstPageLoading

      val mangaListResult = searchMangaUseCase(query.value)
      mangaListResult
        .onSuccess { mangaList ->
          val hasNextPage = mangaList.size >= MANGA_LIST_PER_PAGE_SIZE
          _resultsUiState.value = ResultsUiState.Content(
            mangaList = mangaList,
            currentPage = FIRST_PAGE,
            nextPageState =
              if (!hasNextPage) ResultsNextPageState.NO_MORE_ITEMS
              else ResultsNextPageState.IDLE
          )
        }
        .onFailure {
          _resultsUiState.value = ResultsUiState.FirstPageError
          Log.d(
            TAG,
            "fetchMangaListFirstPage have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  fun fetchMangaListNextPage() {
    when (val currentResultsUiState = _resultsUiState.value) {
      ResultsUiState.FirstPageLoading,
      ResultsUiState.FirstPageError,
        -> return

      is ResultsUiState.Content -> {
        when (currentResultsUiState.nextPageState) {
          ResultsNextPageState.LOADING,
          ResultsNextPageState.NO_MORE_ITEMS
            -> return

          ResultsNextPageState.ERROR -> retryFetchMangaListNextPage()

          ResultsNextPageState.IDLE -> fetchMangaListNextPageInternal(currentResultsUiState)
        }
      }
    }
  }

  private fun fetchMangaListNextPageInternal(currentResultsUiState: ResultsUiState.Content) {
    viewModelScope.launch {
      _resultsUiState.value =
        currentResultsUiState.copy(nextPageState = ResultsNextPageState.LOADING)

      val currentMangaList = currentResultsUiState.mangaList
      val nextPage = currentResultsUiState.currentPage + 1

      val nextMangaListResults = searchMangaUseCase(
        query = query.value,
        offset = currentMangaList.size
      )
      nextMangaListResults
        .onSuccess { nextMangaList ->
          val allMangaList = currentMangaList + nextMangaList
          val hasNextPage = nextMangaList.size >= MANGA_LIST_PER_PAGE_SIZE
          _resultsUiState.value = currentResultsUiState.copy(
            mangaList = allMangaList,
            currentPage = nextPage,
            nextPageState =
              if (!hasNextPage) ResultsNextPageState.NO_MORE_ITEMS
              else ResultsNextPageState.IDLE
          )
        }
        .onFailure {
          _resultsUiState.value =
            currentResultsUiState.copy(nextPageState = ResultsNextPageState.ERROR)
          Log.d(
            TAG,
            "fetchMangaListNextPageInternal have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  fun updateQuery(newQuery: String) {
    if (_query.value == newQuery) return
    _query.value = newQuery
  }

  fun retry() {
    if (_resultsUiState.value is ResultsUiState.FirstPageError)
      fetchMangaListFirstPage()
  }

  fun retryFetchMangaListNextPage() {
    val currentResultsUiState = _resultsUiState.value
    if (currentResultsUiState is ResultsUiState.Content &&
      currentResultsUiState.nextPageState == ResultsNextPageState.ERROR
    ) fetchMangaListNextPage()
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