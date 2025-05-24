package com.decoutkhanqindev.dexreader.ui.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.Manga
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
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val searchMangaUseCase: SearchMangaUseCase
) : ViewModel() {
  private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
  val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

  private val _query = MutableStateFlow("")
  val query: StateFlow<String> = _query.asStateFlow()

  private val _isSuggestionsLoading = MutableStateFlow(false)
  val isSuggestionsLoading: StateFlow<Boolean> = _isSuggestionsLoading.asStateFlow()

  private val searchCachedResults = mutableMapOf<String, List<Manga>>()

  val suggestions = query
    .filter { it.isNotEmpty() }
    .debounce(300L)
    .distinctUntilChanged()
    .flatMapLatest { query ->
      flow {
        _isSuggestionsLoading.value = true

        val cachedResults = searchCachedResults[query.toLowerCase(Locale.ROOT)]
        cachedResults?.let { mangaList ->
          _isSuggestionsLoading.value = false
          val titles = mangaList.map { it.title }
          emit(titles)
        }

        val results = searchMangaUseCase(query)
        results
          .onSuccess { mangaList ->
            _isSuggestionsLoading.value = false
            searchCachedResults[query] = mangaList
            val titles = mangaList.map { it.title }
            emit(titles)
          }
          .onFailure {
            _isSuggestionsLoading.value = false
            emit(emptyList())
            Log.d("SearchViewModel", "suggestions have error: ${it.stackTraceToString()}")
          }
      }
    }
    .stateIn(
      scope = viewModelScope,
      started = WhileSubscribed(5000L),
      initialValue = emptyList()
    )

  fun searchManga(query: String) {
    viewModelScope.launch {
      if (query.isEmpty()) _uiState.value = SearchUiState.Idle

      _uiState.value = SearchUiState.Loading

      val cachedResults = searchCachedResults[query.toLowerCase(Locale.ROOT)]
      cachedResults?.let { mangaList ->
        _uiState.value = SearchUiState.Success(mangaList)
      }

      val results = searchMangaUseCase(query)
      results
        .onSuccess { mangaList ->
          _uiState.value = SearchUiState.Success(mangaList)
        }
        .onFailure {
          _uiState.value = SearchUiState.Error
          Log.d("SearchViewModel", "searchManga have error: ${it.stackTraceToString()}")
        }
    }
  }

  fun onQueryChange(newQuery: String) {
    _query.value = newQuery
  }
}