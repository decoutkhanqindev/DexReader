package com.decoutkhanqindev.dexreader.ui.screens.details

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

  private val _mangaDetailsInfoUiState =
    MutableStateFlow<MangaDetailsInfoUiState>(MangaDetailsInfoUiState.Loading)
  val mangaDetailsInfoUiState: StateFlow<MangaDetailsInfoUiState> = _mangaDetailsInfoUiState.asStateFlow()

  private val _mangaDetailsChaptersUiState =
    MutableStateFlow<MangaDetailsChaptersUiState>(MangaDetailsChaptersUiState.FirstPageLoading)
  val mangaDetailsChaptersUiState: StateFlow<MangaDetailsChaptersUiState> = _mangaDetailsChaptersUiState.asStateFlow()

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
          _mangaDetailsInfoUiState.value = MangaDetailsInfoUiState.Success(manga = it)
        }
        .onFailure {
          _mangaDetailsInfoUiState.value = MangaDetailsInfoUiState.Error
          Log.d("MangaDetailsViewModel", "getManga have error: ${it.stackTraceToString()}")
        }
    }
  }

  private fun fetchChapterListFirstPage() {
    viewModelScope.launch {
      _mangaDetailsChaptersUiState.value = MangaDetailsChaptersUiState.FirstPageLoading

      val chapterList = getChapterListUseCase(
        mangaId = mangaId,
        translatedLanguage = chapterLanguage.value
      )
      chapterList
        .onSuccess {
          val hasNextPage = it.size >= 20
          _mangaDetailsChaptersUiState.value = MangaDetailsChaptersUiState.Content(
            chapterList = it,
            currentPage = 1,
            nextPageState = if (!hasNextPage)
              MangaDetailsChaptersNextPageState.NO_MORE_ITEMS
            else
              MangaDetailsChaptersNextPageState.IDLE
          )
        }
        .onFailure {
          _mangaDetailsChaptersUiState.value = MangaDetailsChaptersUiState.FirstPageError
          Log.d(
            "MangaDetailsViewModel",
            "fetchChapterListFirstPage have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  fun fetchChapterListNextPage() {
    when (val currentUiState = _mangaDetailsChaptersUiState.value) {
      MangaDetailsChaptersUiState.FirstPageLoading,
      MangaDetailsChaptersUiState.FirstPageError,
        -> return

      is MangaDetailsChaptersUiState.Content -> {
        when (currentUiState.nextPageState) {
          MangaDetailsChaptersNextPageState.LOADING,
          MangaDetailsChaptersNextPageState.NO_MORE_ITEMS
            -> return

          MangaDetailsChaptersNextPageState.ERROR -> fetchChapterListFirstPage()

          MangaDetailsChaptersNextPageState.IDLE -> fetchChapterListNextPageInternal(currentUiState)
        }
      }
    }
  }

  private fun fetchChapterListNextPageInternal(currentUiState: MangaDetailsChaptersUiState.Content) {
    viewModelScope.launch {
      _mangaDetailsChaptersUiState.value =
        currentUiState.copy(nextPageState = MangaDetailsChaptersNextPageState.LOADING)
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
          _mangaDetailsChaptersUiState.value = currentUiState.copy(
            chapterList = currentItems + it,
            currentPage = nextPage,
            nextPageState = if (!hasNextPage)
              MangaDetailsChaptersNextPageState.NO_MORE_ITEMS
            else
              MangaDetailsChaptersNextPageState.IDLE
          )
        }
        .onFailure {
          _mangaDetailsChaptersUiState.value = MangaDetailsChaptersUiState.FirstPageError
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

  fun retryFetchChapterListNextPage() {
    fetchChapterListNextPage()
  }
}