package com.decoutkhanqindev.dexreader.presentation.ui.reader

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.manga.chapter.GetChapterDetailsUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.chapter.GetChapterPagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val getChapterDetailsUseCase: GetChapterDetailsUseCase,
  private val getChapterPagesUseCase: GetChapterPagesUseCase,
) : ViewModel() {
  private val chapterId: String = checkNotNull(savedStateHandle[ReaderDestination.chapterIdArg])

  private val _chapterDetailsUiState =
    MutableStateFlow<ChapterDetailsUiState>(ChapterDetailsUiState())
  val chapterDetailsUiState = _chapterDetailsUiState.asStateFlow()

  private val _chapterPagesUiState =
    MutableStateFlow<ChapterPagesUiState>(ChapterPagesUiState.Loading)
  val chapterPagesUiState = _chapterPagesUiState.asStateFlow()

  private var currentChapterPageIndex: Int = 0

  init {
    fetchChapterDetails()
    fetchChapterPages()
  }

  fun fetchChapterDetails() {
    viewModelScope.launch {
      val chapterDetails = getChapterDetailsUseCase(chapterId)
      chapterDetails
        .onSuccess { chapter ->
          _chapterDetailsUiState.update {
            it.copy(
              volume = chapter.volume,
              chapterNumber = chapter.chapterNumber,
              title = chapter.title
            )
          }
        }
        .onFailure {
          Log.d("ReaderViewModel", "fetchChapterDetails have error: ${it.stackTraceToString()}")
        }
    }
  }

  fun fetchChapterPages() {
    viewModelScope.launch {
      _chapterPagesUiState.value = ChapterPagesUiState.Loading

      val chapterPages = getChapterPagesUseCase(chapterId)
      chapterPages
        .onSuccess {
          _chapterPagesUiState.value = ChapterPagesUiState.Success(chapterPages = it)
        }
        .onFailure {
          _chapterPagesUiState.value = ChapterPagesUiState.Error
          Log.d("ReaderViewModel", "fetchChapterPages have error: ${it.stackTraceToString()}")
        }
    }
  }

  fun retry() {
    fetchChapterDetails()
    fetchChapterPages()
  }

  fun updateChapterPage(newChapterPage: Int) {
    currentChapterPageIndex = newChapterPage
    _chapterPagesUiState.update {
      (it as ChapterPagesUiState.Success).copy(
        currentChapterPage = currentChapterPageIndex
      )
    }

    // save history to firebase
  }
}