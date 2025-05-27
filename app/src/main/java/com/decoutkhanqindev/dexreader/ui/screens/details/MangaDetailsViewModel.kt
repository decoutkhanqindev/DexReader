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

  private val _infoUiState =
    MutableStateFlow<MangaInfoUiState>(MangaInfoUiState.Loading)
  val infoUiState: StateFlow<MangaInfoUiState> = _infoUiState.asStateFlow()

  private val _chaptersUiState =
    MutableStateFlow<MangaChaptersUiState>(MangaChaptersUiState.FirstPageLoading)
  val chaptersUiState: StateFlow<MangaChaptersUiState> = _chaptersUiState.asStateFlow()

  private val _selectedLanguage = MutableStateFlow("en")
  val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

  init {
    getMangaDetails()
    loadChapterListFirstPage()
  }

  private fun getMangaDetails() {
    viewModelScope.launch {
      val mangaDetails = getMangaDetailsUseCase(mangaId)
      mangaDetails
        .onSuccess {
          _infoUiState.value = MangaInfoUiState.Success(manga = it)
        }
        .onFailure {
          _infoUiState.value = MangaInfoUiState.Error
          Log.d("MangaDetailsViewModel", "getManga have error: ${it.stackTraceToString()}")
        }
    }
  }

  private fun loadChapterListFirstPage() {
    viewModelScope.launch {
      _chaptersUiState.value = MangaChaptersUiState.FirstPageLoading

      val chapterList = getChapterListUseCase(
        mangaId = mangaId,
        translatedLanguage = selectedLanguage.value
      )
      chapterList
        .onSuccess {
          val hasNextPage = it.size >= 20
          _chaptersUiState.value = MangaChaptersUiState.Content(
            items = it,
            currentPage = 1,
            nextPageState = if (!hasNextPage)
              MangaChaptersNextPageState.NO_MORE_ITEMS
            else
              MangaChaptersNextPageState.IDLE
          )
        }
        .onFailure {
          _chaptersUiState.value = MangaChaptersUiState.FirstPageError
          Log.d(
            "MangaDetailsViewModel",
            "loadChapterListFirstPage have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  fun loadChapterListNextPage() {
    when (val currentUiState = _chaptersUiState.value) {
      MangaChaptersUiState.FirstPageLoading,
      MangaChaptersUiState.FirstPageError,
        -> return

      is MangaChaptersUiState.Content -> {
        when (currentUiState.nextPageState) {
          MangaChaptersNextPageState.LOADING,
          MangaChaptersNextPageState.NO_MORE_ITEMS
            -> return

          MangaChaptersNextPageState.ERROR -> loadChapterListFirstPage()

          MangaChaptersNextPageState.IDLE -> loadChapterListNextPageInternal(currentUiState)
        }
      }
    }
  }

  private fun loadChapterListNextPageInternal(currentUiState: MangaChaptersUiState.Content) {
    viewModelScope.launch {
      _chaptersUiState.value =
        currentUiState.copy(nextPageState = MangaChaptersNextPageState.LOADING)
      val currentItems = currentUiState.items
      val nextPage: Int = currentUiState.currentPage + 1

      val nextChapterList = getChapterListUseCase(
        mangaId = mangaId,
        offset = currentItems.size,
        translatedLanguage = selectedLanguage.value
      )
      nextChapterList
        .onSuccess {
          val hasNextPage = it.size >= 20
          _chaptersUiState.value = currentUiState.copy(
            items = currentItems + it,
            currentPage = nextPage,
            nextPageState = if (!hasNextPage)
              MangaChaptersNextPageState.NO_MORE_ITEMS
            else
              MangaChaptersNextPageState.IDLE
          )
        }
        .onFailure {
          _chaptersUiState.value = MangaChaptersUiState.FirstPageError
          Log.d(
            "MangaDetailsViewModel",
            "loadChapterListNextPageInternal have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  fun setChapterLanguage(language: String) {
    if (language == _selectedLanguage.value) return
    _selectedLanguage.value = language
    loadChapterListFirstPage()
  }

  fun retry() {
    getMangaDetails()
    loadChapterListFirstPage()
  }

  fun retryLoadNextChapterListPage() {
    loadChapterListNextPage()
  }
}
