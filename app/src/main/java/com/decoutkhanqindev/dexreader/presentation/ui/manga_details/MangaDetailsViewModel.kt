package com.decoutkhanqindev.dexreader.presentation.ui.manga_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetChapterListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetMangaDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaDetailsViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val getMangaDetailsUseCase: GetMangaDetailsUseCase,
  private val getChapterListUseCase: GetChapterListUseCase
) : ViewModel() {
  private val mangaId: String = checkNotNull(savedStateHandle[MangaDetailsDestination.mangaIdArg])

  private val _mangaDetailsUiState =
    MutableStateFlow<MangaDetailsUiState>(MangaDetailsUiState.Loading)
  val mangaDetailsUiState: StateFlow<MangaDetailsUiState> = _mangaDetailsUiState.asStateFlow()

  private val _mangaChaptersUiState =
    MutableStateFlow<MangaChaptersUiState>(MangaChaptersUiState.FirstPageLoading)
  val mangaChaptersUiState: StateFlow<MangaChaptersUiState> = _mangaChaptersUiState.asStateFlow()

  private val _chapterLanguage = MutableStateFlow("en")
  val chapterLanguage: StateFlow<String> = _chapterLanguage.asStateFlow()

  init {
    fetchMangaDetails()
    fetchChapterListFirstPage()
  }

  private fun fetchMangaDetails() {
    viewModelScope.launch {
      val mangaDetails = getMangaDetailsUseCase(mangaId)
      mangaDetails
        .onSuccess {
          _mangaDetailsUiState.value = MangaDetailsUiState.Success(manga = it)
        }
        .onFailure {
          _mangaDetailsUiState.value = MangaDetailsUiState.Error
          Log.d("MangaDetailsViewModel", "getManga have error: ${it.stackTraceToString()}")
        }
    }
  }

  private fun fetchChapterListFirstPage() {
    viewModelScope.launch {
      _mangaChaptersUiState.value = MangaChaptersUiState.FirstPageLoading

      val chapterList = getChapterListUseCase(
        mangaId = mangaId,
        translatedLanguage = chapterLanguage.value
      )
      chapterList
        .onSuccess {
          val hasNextPage = it.size >= 20
          _mangaChaptersUiState.value = MangaChaptersUiState.Content(
            chapterList = it,
            currentPage = 1,
            nextPageState = if (!hasNextPage)
              MangaChaptersNextPageState.NO_MORE_ITEMS
            else
              MangaChaptersNextPageState.IDLE
          )
        }
        .onFailure {
          _mangaChaptersUiState.value = MangaChaptersUiState.FirstPageError
          Log.d(
            "MangaDetailsViewModel",
            "fetchChapterListFirstPage have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  fun fetchChapterListNextPage() {
    when (val currentUiState = _mangaChaptersUiState.value) {
      MangaChaptersUiState.FirstPageLoading,
      MangaChaptersUiState.FirstPageError,
        -> return

      is MangaChaptersUiState.Content -> {
        when (currentUiState.nextPageState) {
          MangaChaptersNextPageState.LOADING,
          MangaChaptersNextPageState.NO_MORE_ITEMS
            -> return

          MangaChaptersNextPageState.ERROR -> fetchChapterListFirstPage()

          MangaChaptersNextPageState.IDLE -> fetchChapterListNextPageInternal(currentUiState)
        }
      }
    }
  }

  private fun fetchChapterListNextPageInternal(currentUiState: MangaChaptersUiState.Content) {
    viewModelScope.launch {
      _mangaChaptersUiState.value =
        currentUiState.copy(nextPageState = MangaChaptersNextPageState.LOADING)
      val currentItems = currentUiState.chapterList
      val nextPage: Int = currentUiState.currentPage + 1

      val nextChapterList = getChapterListUseCase(
        mangaId = mangaId,
        offset = currentItems.size,
        translatedLanguage = chapterLanguage.value
      )
      nextChapterList
        .onSuccess {
          val hasNextPage = it.size >= 20
          _mangaChaptersUiState.value = currentUiState.copy(
            chapterList = currentItems + it,
            currentPage = nextPage,
            nextPageState = if (!hasNextPage)
              MangaChaptersNextPageState.NO_MORE_ITEMS
            else
              MangaChaptersNextPageState.IDLE
          )
        }
        .onFailure {
          _mangaChaptersUiState.value = MangaChaptersUiState.FirstPageError
          Log.d(
            "MangaDetailsViewModel",
            "fetchChapterListNextPageInternal have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  fun updateChapterLanguage(language: String) {
    if (language == _chapterLanguage.value) return
    _chapterLanguage.value = language
    fetchChapterListFirstPage()
  }

  fun retry() {
    fetchMangaDetails()
    fetchChapterListFirstPage()
  }

  fun retryFetchChapterListFirstPage() {
    fetchChapterListFirstPage()
  }

  fun retryFetchChapterListNextPage() {
    fetchChapterListNextPage()
  }
}