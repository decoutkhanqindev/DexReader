package com.decoutkhanqindev.dexreader.ui.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.manga.SearchMangaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
    MutableStateFlow<SearchSuggestionsUiState>(SearchSuggestionsUiState.Loading)
  val suggestionsUiState: StateFlow<SearchSuggestionsUiState> = _suggestionsUiState.asStateFlow()

  private val _resultsUiState =
    MutableStateFlow<SearchResultsUiState>(SearchResultsUiState.FirstPageLoading)
  val resultsUiState: StateFlow<SearchResultsUiState> = _resultsUiState.asStateFlow()

  private val _query = MutableStateFlow("")
  val query: StateFlow<String> = _query.asStateFlow()

  val suggestionList = query
    .filter { it.isNotEmpty() }
    .debounce(500L)
    .distinctUntilChanged()
    .flatMapLatest { query ->
      flow {
        _suggestionsUiState.value = SearchSuggestionsUiState.Loading

        val results = searchMangaUseCase(query)
        results
          .onSuccess { mangaList ->
            val titles = mangaList.map { it.title }.take(10)
            _suggestionsUiState.value = SearchSuggestionsUiState.Success
            emit(titles)
          }
          .onFailure {
            _suggestionsUiState.value = SearchSuggestionsUiState.Error
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

  fun loadSearchMangaListFirstPage() {
    viewModelScope.launch {
      _resultsUiState.value = SearchResultsUiState.FirstPageLoading

      val mangaList = searchMangaUseCase(query.value)
      Log.d("SearchViewModel", "loadSearchMangaListFirstPage - query: ${query.value}")
      mangaList
        .onSuccess {
          val hasNextPage = it.size >= 20
          _resultsUiState.value = SearchResultsUiState.Content(
            items = it,
            currentPage = 1,
            nextPageState = if (!hasNextPage)
              SearchMangaListNextPageState.NO_MORE_ITEMS
            else
              SearchMangaListNextPageState.IDLE
          )
          Log.d("SearchViewModel", "loadSearchMangaListFirstPage - mangaList: ${it.size}")
          Log.d(
            "SearchViewModel",
            "loadSearchMangaListFirstPage - nextPageState: ${(_resultsUiState.value as SearchResultsUiState.Content).nextPageState}"
          )
        }
        .onFailure {
          _resultsUiState.value = SearchResultsUiState.FirstPageError
          Log.d(
            "SearchViewModel",
            "loadSearchMangaListFirstPage have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  fun loadSearchMangaListNextPage() {
    Log.d(
      "SearchViewModel",
      "loadSearchMangaListNextPage - nextPageState: ${(_resultsUiState.value as SearchResultsUiState.Content).nextPageState}"
    )
    when (val currentUiState = _resultsUiState.value) {
      SearchResultsUiState.FirstPageLoading,
      SearchResultsUiState.FirstPageError,
        -> return

      is SearchResultsUiState.Content -> {
        when (currentUiState.nextPageState) {
          SearchMangaListNextPageState.LOADING,
          SearchMangaListNextPageState.NO_MORE_ITEMS
            -> return

          SearchMangaListNextPageState.ERROR -> loadSearchMangaListFirstPage()

          SearchMangaListNextPageState.IDLE -> loadSearchMangaListNextPageInternal(currentUiState)
        }
      }
    }
    Log.d(
      "SearchViewModel",
      "loadSearchMangaListNextPage - nextPageState: ${(_resultsUiState.value as SearchResultsUiState.Content).nextPageState}"
    )
  }

  private fun loadSearchMangaListNextPageInternal(currentUiState: SearchResultsUiState.Content) {
    viewModelScope.launch {
      _resultsUiState.value =
        currentUiState.copy(nextPageState = SearchMangaListNextPageState.LOADING)
      val currentItems = currentUiState.items
      val nextPage = currentUiState.currentPage + 1

      val nextMangaList = searchMangaUseCase(
        query = query.value,
        offset = currentItems.size
      )
      Log.d("SearchViewModel", "loadSearchMangaListNextPageInternal - query: ${query.value}")
      nextMangaList
        .onSuccess {
          val hasNextPage = it.size >= 20
          _resultsUiState.value = currentUiState.copy(
            items = currentItems + it,
            currentPage = nextPage,
            nextPageState = if (!hasNextPage)
              SearchMangaListNextPageState.NO_MORE_ITEMS
            else
              SearchMangaListNextPageState.IDLE
          )
          Log.d("SearchViewModel", "loadSearchMangaListFirstPage - mangaList: ${it.size}")
          Log.d(
            "SearchViewModel",
            "loadSearchMangaListFirstPage - nextPageState: ${(_resultsUiState.value as SearchResultsUiState.Content).nextPageState}"
          )
        }
        .onFailure {
          _resultsUiState.value =
            currentUiState.copy(nextPageState = SearchMangaListNextPageState.ERROR)
          Log.d(
            "SearchViewModel",
            "loadSearchMangaListNextPageInternal have error: ${it.stackTraceToString()}"
          )
        }
    }
  }


  fun onQueryChange(newQuery: String) {
    if (newQuery == query.value) return
    _query.value = newQuery
  }

  fun retry() {
    loadSearchMangaListFirstPage()
  }

  fun retryLoadSearchMangaListNextPage() {
    loadSearchMangaListNextPage()
  }
}