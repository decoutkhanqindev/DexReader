package com.decoutkhanqindev.dexreader.presentation.ui.manga_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.chapter.GetChapterListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetMangaDetailsUseCase
import com.decoutkhanqindev.dexreader.presentation.navigation.NavigationDestination
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
  private val getChapterListUseCase: GetChapterListUseCase,
) : ViewModel() {
  private val mangaIdFromArg: String =
    checkNotNull(savedStateHandle[NavigationDestination.MangaDetailsDestination.MANGA_ID_ARG])

  private val _mangaDetailsUiState =
    MutableStateFlow<MangaDetailsUiState>(MangaDetailsUiState.Loading)
  val mangaDetailsUiState: StateFlow<MangaDetailsUiState> = _mangaDetailsUiState.asStateFlow()

  private val _mangaChaptersUiState =
    MutableStateFlow<MangaChaptersUiState>(MangaChaptersUiState.FirstPageLoading)
  val mangaChaptersUiState: StateFlow<MangaChaptersUiState> = _mangaChaptersUiState.asStateFlow()

  private val _firstChapterId = MutableStateFlow<String?>(null)
  val firstChapterId = _firstChapterId.asStateFlow()

  private val _chapterLanguage = MutableStateFlow("en")
  val chapterLanguage: StateFlow<String> = _chapterLanguage.asStateFlow()

  init {
    fetchMangaDetails()
    fetchFirstChapter()
    fetchChapterListFirstPage()
  }

  private fun fetchMangaDetails() {
    viewModelScope.launch {
      val mangaDetails = getMangaDetailsUseCase(mangaIdFromArg)
      mangaDetails
        .onSuccess {
          _mangaDetailsUiState.value = MangaDetailsUiState.Success(manga = it)
        }
        .onFailure {
          _mangaDetailsUiState.value = MangaDetailsUiState.Error
          Log.d(TAG, "fetchMangaDetails have error: ${it.stackTraceToString()}")
        }
    }
  }

  private fun fetchFirstChapter() {
    viewModelScope.launch {
      val chapterListResult = getChapterListUseCase(
        mangaId = mangaIdFromArg,
        limit = 1,
        translatedLanguage = chapterLanguage.value,
        volumeOrder = ASC_ORDER,
        chapterOrder = ASC_ORDER
      )
      chapterListResult
        .onSuccess {
          if (it.isNotEmpty()) {
            _firstChapterId.value = it.first().id
          } else {
            _firstChapterId.value = null
          }
        }
        .onFailure {
          _firstChapterId.value = null
          Log.d(
            TAG,
            "fetchFirstChapter have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  private fun fetchChapterListFirstPage() {
    viewModelScope.launch {
      _mangaChaptersUiState.value = MangaChaptersUiState.FirstPageLoading

      val chapterListResult = getChapterListUseCase(
        mangaId = mangaIdFromArg,
        translatedLanguage = chapterLanguage.value
      )
      chapterListResult
        .onSuccess { chapterList ->
          val hasNextPage = chapterList.size >= CHAPTER_LIST_PER_PAGE_SIZE
          _mangaChaptersUiState.value = MangaChaptersUiState.Content(
            chapterList = chapterList,
            currentPage = FIRST_PAGE,
            nextPageState = if (!hasNextPage) MangaChaptersNextPageState.NO_MORE_ITEMS
            else MangaChaptersNextPageState.IDLE
          )
        }
        .onFailure {
          _mangaChaptersUiState.value = MangaChaptersUiState.FirstPageError
          Log.d(
            TAG,
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

          MangaChaptersNextPageState.ERROR -> retryFetchChapterListNextPage()

          MangaChaptersNextPageState.IDLE -> fetchChapterListNextPageInternal(currentUiState = currentUiState)
        }
      }
    }
  }

  private fun fetchChapterListNextPageInternal(currentUiState: MangaChaptersUiState.Content) {
    viewModelScope.launch {
      _mangaChaptersUiState.value =
        currentUiState.copy(nextPageState = MangaChaptersNextPageState.LOADING)

      val currentMangaList = currentUiState.chapterList
      val nextPage: Int = currentUiState.currentPage + 1

      val nextChapterListResult = getChapterListUseCase(
        mangaId = mangaIdFromArg,
        offset = currentMangaList.size,
        translatedLanguage = chapterLanguage.value
      )
      nextChapterListResult
        .onSuccess { nextChapterList ->
          val updatedChapterList = currentMangaList + nextChapterList
          val hasNextPage = nextChapterList.size >= CHAPTER_LIST_PER_PAGE_SIZE
          _mangaChaptersUiState.value = currentUiState.copy(
            chapterList = updatedChapterList,
            currentPage = nextPage,
            nextPageState = if (!hasNextPage) MangaChaptersNextPageState.NO_MORE_ITEMS
            else MangaChaptersNextPageState.IDLE
          )
        }
        .onFailure {
          _mangaChaptersUiState.value =
            currentUiState.copy(nextPageState = MangaChaptersNextPageState.ERROR)
          Log.d(
            TAG,
            "fetchChapterListNextPageInternal have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  fun updateChapterLanguage(language: String) {
    if (language == _chapterLanguage.value) return
    _chapterLanguage.value = language
    fetchFirstChapter()
    fetchChapterListFirstPage()
  }

  fun retry() {
    fetchMangaDetails()
    fetchFirstChapter()
    fetchChapterListFirstPage()
  }

  fun retryFetchChapterListFirstPage() {
    fetchChapterListFirstPage()
  }

  fun retryFetchChapterListNextPage() {
    val currentUiState = _mangaChaptersUiState.value
    if (currentUiState is MangaChaptersUiState.Content &&
      currentUiState.nextPageState == MangaChaptersNextPageState.ERROR
    ) {
      fetchChapterListNextPageInternal(currentUiState)
    }
  }

  companion object {
    private const val TAG = "MangaDetailsViewModel"
    private const val FIRST_PAGE = 1
    private const val ASC_ORDER = "asc"
    private const val CHAPTER_LIST_PER_PAGE_SIZE = 20
  }
}