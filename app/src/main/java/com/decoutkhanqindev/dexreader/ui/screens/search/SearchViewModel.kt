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
  private val _mangaSuggestionsUiState =
    MutableStateFlow<SearchMangaSuggestionsUiState>(SearchMangaSuggestionsUiState.Loading)
  val mangaSuggestionsUiState: StateFlow<SearchMangaSuggestionsUiState> =
    _mangaSuggestionsUiState.asStateFlow()

  private val _mangaResultsUiState =
    MutableStateFlow<SearchMangaResultsUiState>(SearchMangaResultsUiState.FirstPageLoading)
  val mangaResultsUiState: StateFlow<SearchMangaResultsUiState> = _mangaResultsUiState.asStateFlow()

  private val _query = MutableStateFlow("")
  val query: StateFlow<String> = _query.asStateFlow()

  val mangaSuggestions = query
    .filter { it.isNotEmpty() }
    .debounce(500L)
    .distinctUntilChanged()
    .flatMapLatest { query ->
      flow {
        _mangaSuggestionsUiState.value = SearchMangaSuggestionsUiState.Loading

        val mangaList = searchMangaUseCase(query)
        mangaList
          .onSuccess {
            val titleList = it.map { manga -> manga.title }.take(10)
            _mangaSuggestionsUiState.value = SearchMangaSuggestionsUiState.Success
            emit(titleList)
          }
          .onFailure {
            _mangaSuggestionsUiState.value = SearchMangaSuggestionsUiState.Error
            emit(emptyList())
            Log.d("SearchViewModel", "mangaSuggestions have error: ${it.stackTraceToString()}")
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
      _mangaResultsUiState.value = SearchMangaResultsUiState.FirstPageLoading

      val mangaList = searchMangaUseCase(query.value)
      mangaList
        .onSuccess {
          val hasNextPage = it.size >= 20
          _mangaResultsUiState.value = SearchMangaResultsUiState.Content(
            mangaList = it,
            currentPage = 1,
            nextPageState = if (!hasNextPage)
              SearchMangaResultsNextPageState.NO_MORE_ITEMS
            else
              SearchMangaResultsNextPageState.IDLE
          )
        }
        .onFailure {
          _mangaResultsUiState.value = SearchMangaResultsUiState.FirstPageError
          Log.d(
            "SearchViewModel",
            "fetchMangaListFirstPage have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  fun fetchMangaListNextPage() {
    when (val currentUiState = _mangaResultsUiState.value) {
      SearchMangaResultsUiState.FirstPageLoading,
      SearchMangaResultsUiState.FirstPageError,
        -> return

      is SearchMangaResultsUiState.Content -> {
        when (currentUiState.nextPageState) {
          SearchMangaResultsNextPageState.LOADING,
          SearchMangaResultsNextPageState.NO_MORE_ITEMS
            -> return

          SearchMangaResultsNextPageState.ERROR -> fetchMangaListFirstPage()

          SearchMangaResultsNextPageState.IDLE -> fetchMangaListNextPageInternal(currentUiState)
        }
      }
    }
  }

  private fun fetchMangaListNextPageInternal(currentUiState: SearchMangaResultsUiState.Content) {
    viewModelScope.launch {
      _mangaResultsUiState.value =
        currentUiState.copy(nextPageState = SearchMangaResultsNextPageState.LOADING)
      val currentItems = currentUiState.mangaList
      val nextPage = currentUiState.currentPage + 1

      val nextMangaList = searchMangaUseCase(
        query = query.value,
        offset = currentItems.size
      )
      nextMangaList
        .onSuccess {
          val hasNextPage = it.size >= 20
          _mangaResultsUiState.value = currentUiState.copy(
            mangaList = currentItems + it,
            currentPage = nextPage,
            nextPageState = if (!hasNextPage)
              SearchMangaResultsNextPageState.NO_MORE_ITEMS
            else
              SearchMangaResultsNextPageState.IDLE
          )
        }
        .onFailure {
          _mangaResultsUiState.value =
            currentUiState.copy(nextPageState = SearchMangaResultsNextPageState.ERROR)
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
    fetchMangaListNextPage()
  }
}