package com.decoutkhanqindev.dexreader.presentation.ui.reader

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.usecase.manga.chapter.GetChapterDetailsUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.chapter.GetChapterListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.chapter.GetChapterPagesUseCase
import com.decoutkhanqindev.dexreader.utils.toLanguageCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val getChapterListUseCase: GetChapterListUseCase,
  private val getChapterDetailsUseCase: GetChapterDetailsUseCase,
  private val getChapterPagesUseCase: GetChapterPagesUseCase,
) : ViewModel() {
  private val chapterIdFromArg: String =
    checkNotNull(savedStateHandle[ReaderDestination.chapterIdArg])
  private var currentChapterId: String = chapterIdFromArg

  private val _chapterDetailsState =
    MutableStateFlow<ChapterDetailsUiState>(ChapterDetailsUiState())
  val chapterDetailsState = _chapterDetailsState.asStateFlow()

  private val _chapterPagesUiState =
    MutableStateFlow<ChapterPagesUiState>(ChapterPagesUiState.Loading)
  val chapterPagesUiState = _chapterPagesUiState.asStateFlow()

  private val _chapterNavState =
    MutableStateFlow(ChapterNavigationState(currentChapterId = currentChapterId))
  val chapterNavState = _chapterNavState.asStateFlow()

  private var mangaId: String? = null
  private var chapterLanguage: String? = null
  private var currentChapterList: List<Chapter> = emptyList()
  private var hasNextChapterListPage = true
  private var isFetchingChapterList = false

  init {
    fetchChapterDetails()
    fetchChapterPages()
  }

  private fun fetchChapterDetails() {
    viewModelScope.launch {
      val chapterDetailsResult = getChapterDetailsUseCase(currentChapterId)
      chapterDetailsResult
        .onSuccess { chapter ->
          _chapterDetailsState.update {
            it.copy(
              volume = chapter.volume,
              chapterNumber = chapter.chapterNumber,
              title = chapter.title
            )
          }
          if (currentChapterList.isEmpty()) {
            mangaId = chapter.mangaId
            chapterLanguage = chapter.translatedLanguage.toLanguageCode()
            fetchChapterListFirstPage()
          }
        }
        .onFailure {
          Log.e(
            "ReaderViewModel",
            "fetchChapterDetails have error: ${it.stackTraceToString()}"
          )
          _chapterDetailsState.update { ChapterDetailsUiState() }
        }
    }
  }

  private fun fetchChapterPages() {
    viewModelScope.launch {
      _chapterPagesUiState.value = ChapterPagesUiState.Loading

      val chapterPagesResult = getChapterPagesUseCase(currentChapterId)
      chapterPagesResult
        .onSuccess {
          _chapterPagesUiState.value = ChapterPagesUiState.Success(chapterPages = it)
        }
        .onFailure {
          _chapterPagesUiState.value = ChapterPagesUiState.Error
          Log.e("ReaderViewModel", "fetchChapterPages have error: ${it.stackTraceToString()}")
        }
    }
  }

  private fun fetchChapterListFirstPage() {
    if (isFetchingChapterList) return

    viewModelScope.launch {
      isFetchingChapterList = true

      val chapterListResult = getChapterListUseCase(
        mangaId = mangaId!!,
        translatedLanguage = chapterLanguage!!,
        volumeOrder = "asc",
        chapterOrder = "asc",
      )
      chapterListResult
        .onSuccess { chapterList ->
          isFetchingChapterList = false
          currentChapterList = chapterList
          hasNextChapterListPage = currentChapterList.size >= 20
          updateChapterNavUiState()
        }
        .onFailure {
          isFetchingChapterList = false
          currentChapterList = emptyList()
          Log.d(
            "ReaderViewModel",
            "fetchChapterListFirstPage have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  private fun fetchChapterListNextPage() {
    if (!hasNextChapterListPage || isFetchingChapterList) return

    viewModelScope.launch {
      isFetchingChapterList = true

      val chapterListResult = getChapterListUseCase(
        mangaId = mangaId!!,
        offset = currentChapterList.size,
        translatedLanguage = chapterLanguage!!,
        volumeOrder = "asc",
        chapterOrder = "asc",
      )
      chapterListResult
        .onSuccess { nextChapterList ->
          isFetchingChapterList = false
          currentChapterList += nextChapterList
          hasNextChapterListPage = nextChapterList.size >= 20
          updateChapterNavUiState()
        }
        .onFailure {
          isFetchingChapterList = false
          Log.d(
            "ReaderViewModel",
            "fetchChapterListNextPage have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  private fun updateChapterNavUiState() {
    viewModelScope.launch {
      val currentChapterIndex = currentChapterList.indexOfFirst { it.id == currentChapterId }
      if (currentChapterIndex == -1) {
        _chapterNavState.update { it.copy(canNavigateNext = hasNextChapterListPage) }
        if (hasNextChapterListPage) {
          fetchChapterListNextPage()
        }
        return@launch
      }

      val isFirstChapter = currentChapterIndex == 0
      val isLastChapter = currentChapterIndex == currentChapterList.lastIndex

      _chapterNavState.update {
        it.copy(
          currentChapterId = currentChapterId,
          previousChapterId = if (!isFirstChapter) currentChapterList[currentChapterIndex - 1].id else null,
          nextChapterId = if (!isLastChapter) currentChapterList[currentChapterIndex + 1].id else null,
          canNavigatePrevious = !isFirstChapter,
          canNavigateNext = !isLastChapter || hasNextChapterListPage
        )
      }

      val isNearLastChapter = currentChapterIndex == (currentChapterList.lastIndex - 5)
      if (isNearLastChapter && hasNextChapterListPage) {
        fetchChapterListNextPage()
      }
    }
  }

  fun navigateToNextChapter() {
    val currentState = _chapterNavState.value
    if (currentState.nextChapterId != null) {
      currentChapterId = currentState.nextChapterId
      updateChapterNavUiState()
      fetchChapterDetails()
      fetchChapterPages()
    } else if (hasNextChapterListPage) {
      fetchChapterListNextPage()
    }
  }

  fun navigateToPreviousChapter() {
    val currentState = _chapterNavState.value
    currentState.previousChapterId?.let { previousId ->
      currentChapterId = previousId
      updateChapterNavUiState()
      fetchChapterDetails()
      fetchChapterPages()
    }
  }

  fun retry() {
    fetchChapterDetails()
    fetchChapterPages()
  }

  fun updateChapterPage(newChapterPage: Int) {
    _chapterPagesUiState.update {
      (it as? ChapterPagesUiState.Success)?.copy(
        currentChapterPage = newChapterPage
      ) ?: it
    }

    // save history to firebase
  }
}