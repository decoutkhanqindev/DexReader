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
    .debounce(500L)
    .distinctUntilChanged()
    .flatMapLatest { query ->
      flow {
        _suggestionsUiState.value = SuggestionsUiState.Loading

        val mangaListResult = searchMangaUseCase(query)
        mangaListResult
          .onSuccess { mangaList ->
            val titleList = mangaList.map { manga -> manga.title }.take(10)
            _suggestionsUiState.value = SuggestionsUiState.Success
            emit(titleList)
          }
          .onFailure {
            _suggestionsUiState.value = SuggestionsUiState.Error
            emit(emptyList())
            Log.d("SearchViewModel", "suggestionList have error: ${it.stackTraceToString()}")
          }
      }
    }
    .stateIn(
      scope = viewModelScope,
      started = WhileSubscribed(5000L),
      initialValue = emptyList()
    )

  fun fetchMangaListFirstPage() {
    viewModelScope.launch {
      _resultsUiState.value = ResultsUiState.FirstPageLoading

      val mangaListResult = searchMangaUseCase(query.value)
      mangaListResult
        .onSuccess { mangaList ->
          val hasNextPage = mangaList.size >= 20
          _resultsUiState.value = ResultsUiState.Content(
            results = mangaList,
            currentPage = 1,
            nextPageState = if (!hasNextPage)
              ResultsNextPageState.NO_MORE_ITEMS
            else
              ResultsNextPageState.IDLE
          )
        }
        .onFailure {
          _resultsUiState.value = ResultsUiState.FirstPageError
          Log.d(
            "SearchViewModel",
            "fetchMangaListFirstPage have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  fun fetchMangaListNextPage() {
    when (val currentUiState = _resultsUiState.value) {
      ResultsUiState.FirstPageLoading,
      ResultsUiState.FirstPageError,
        -> return

      is ResultsUiState.Content -> {
        when (currentUiState.nextPageState) {
          ResultsNextPageState.LOADING,
          ResultsNextPageState.NO_MORE_ITEMS
            -> return

          ResultsNextPageState.ERROR -> fetchMangaListFirstPage()

          ResultsNextPageState.IDLE -> fetchMangaListNextPageInternal(currentUiState)
        }
      }
    }
  }

  private fun fetchMangaListNextPageInternal(currentUiState: ResultsUiState.Content) {
    viewModelScope.launch {
      _resultsUiState.value =
        currentUiState.copy(nextPageState = ResultsNextPageState.LOADING)
      val currentMangaList = currentUiState.results
      val nextPage = currentUiState.currentPage + 1

      val nextMangaListResults = searchMangaUseCase(
        query = query.value,
        offset = currentMangaList.size
      )
      nextMangaListResults
        .onSuccess { nextMangaList ->
          val allMangaList = currentMangaList + nextMangaList
          val hasNextPage = nextMangaList.size >= 20
          _resultsUiState.value = currentUiState.copy(
            results = currentMangaList + allMangaList,
            currentPage = nextPage,
            nextPageState = if (!hasNextPage)
              ResultsNextPageState.NO_MORE_ITEMS
            else
              ResultsNextPageState.IDLE
          )
        }
        .onFailure {
          _resultsUiState.value = currentUiState.copy(nextPageState = ResultsNextPageState.ERROR)
          Log.d(
            "SearchViewModel",
            "fetchMangaListNextPageInternal have error: ${it.stackTraceToString()}"
          )
        }
    }
  }


  fun updateQuery(newQuery: String) {
    if (newQuery == query.value) return
    _query.value = newQuery
  }

  fun retry() {
    fetchMangaListFirstPage()
  }

  fun retryFetchMangaListNextPage() {
    val currentUiState = _resultsUiState.value
    if (currentUiState is ResultsUiState.Content && currentUiState.nextPageState == ResultsNextPageState.ERROR) {
      fetchMangaListNextPageInternal(currentUiState)
    }
  }
}