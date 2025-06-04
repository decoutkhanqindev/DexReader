package com.decoutkhanqindev.dexreader.presentation.ui.reader

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.usecase.cache.AddChapterCacheUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.cache.ClearExpiredCacheUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.cache.GetChapterCacheUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.chapter.GetChapterDetailsUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.chapter.GetChapterListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.chapter.GetChapterPagesUseCase
import com.decoutkhanqindev.dexreader.utils.toLanguageCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
  private val addChapterCacheUseCase: AddChapterCacheUseCase,
  private val getChapterCacheUseCase: GetChapterCacheUseCase,
  private val clearExpiredCacheUseCase: ClearExpiredCacheUseCase
) : ViewModel() {
  private val chapterIdFromArg: String =
    checkNotNull(savedStateHandle[ReaderDestination.chapterIdArg])
  private var currentChapterId: String = chapterIdFromArg

  private val _chapterDetailsState =
    MutableStateFlow<ChapterDetailsUiState>(ChapterDetailsUiState())
  val chapterDetailsState: StateFlow<ChapterDetailsUiState> = _chapterDetailsState.asStateFlow()

  private val _chapterPagesUiState =
    MutableStateFlow<ChapterPagesUiState>(ChapterPagesUiState.Loading)
  val chapterPagesUiState: StateFlow<ChapterPagesUiState> = _chapterPagesUiState.asStateFlow()

  private val _chapterNavState =
    MutableStateFlow(ChapterNavigationState(currentChapterId = currentChapterId))
  val chapterNavState: StateFlow<ChapterNavigationState> = _chapterNavState.asStateFlow()

  private var mangaId: String? = null
  private var chapterLanguage: String? = null
  private var currentChapterList: List<Chapter> = emptyList()
  private var hasNextChapterListPage = true
  private var isFetchingChapterList = false

  init {
    clearExpiredCache()
    fetchChapterDetails()
    fetchChapterPages()
  }

  private fun clearExpiredCache() {
    viewModelScope.launch {
      val expiryTimestamp = System.currentTimeMillis() - CACHE_EXPIRY_DURATION_MILLIS
      val clearExpiredCacheResult = clearExpiredCacheUseCase(expiryTimestamp)
      clearExpiredCacheResult
        .onSuccess {
          Log.d(TAG, "Clear expired cache successfully.")
        }
        .onFailure {
          Log.d(TAG, "Clear expired cache have error: ${it.stackTraceToString()}")
        }
    }
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

          mangaId = chapter.mangaId
          chapterLanguage = chapter.translatedLanguage.toLanguageCode()
          if (currentChapterList.isEmpty() && mangaId != null && chapterLanguage != null) {
            fetchChapterListFirstPage()
          }
        }
        .onFailure {
          _chapterDetailsState.update { ChapterDetailsUiState() }
          Log.e(
            TAG,
            "fetchChapterDetails have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  private fun fetchChapterPages(isPrefetch: Boolean = false) {
    val chapterIdToFetch = if (isPrefetch)
      _chapterNavState.value.nextChapterId ?: return
    else
      currentChapterId

    if (chapterIdToFetch.isBlank()) return

    viewModelScope.launch {
      if (!isPrefetch) _chapterPagesUiState.value = ChapterPagesUiState.Loading

      val cachedResult = getChapterCacheUseCase(chapterIdToFetch)
      cachedResult
        .onSuccess { chapterPages ->
          if (!isPrefetch) {
            _chapterPagesUiState.value = ChapterPagesUiState.Success(chapterPages = chapterPages)
            prefetchNextChapterPages()
          }
          return@launch
        }
        .onFailure {
          Log.d(TAG, "Fetch chapter from cache have error: ${it.stackTraceToString()}")
        }

      val networkResult = getChapterPagesUseCase(chapterIdToFetch)
      networkResult
        .onSuccess { chapterPages ->
          if (!isPrefetch)
            _chapterPagesUiState.value = ChapterPagesUiState.Success(chapterPages = chapterPages)

          if (mangaId != null) {
            addChapterCacheUseCase(mangaId = mangaId!!, chapterPages = chapterPages)
              .onSuccess {
                Log.d(TAG, "Add chapter to cache successfully.")
              }
              .onFailure {
                Log.d(TAG, "Add chapter to cache have error: ${it.stackTraceToString()}")
              }
          }

          if (!isPrefetch) prefetchNextChapterPages()
        }
        .onFailure {
          if (!isPrefetch) _chapterPagesUiState.value = ChapterPagesUiState.Error
          Log.d(TAG, "Fetch chapter from network have error: ${it.stackTraceToString()}")
        }
    }
  }

  private fun prefetchNextChapterPages() {
    val currentState = _chapterNavState.value
    currentState.nextChapterId?.let { nextId ->
      if (nextId.isNotBlank()) {
        if (mangaId == null) return
        fetchChapterPages(isPrefetch = true)
      }
    }
  }

  fun navigateToPreviousChapter() {
    val currentState = _chapterNavState.value
    currentState.previousChapterId?.let { previousId ->
      currentChapterId = previousId
      fetchChapterDetails()
      fetchChapterPages()
      updateChapterNavState()
    }
  }

  fun navigateToNextChapter() {
    val currentState = _chapterNavState.value
    if (currentState.nextChapterId != null) {
      currentChapterId = currentState.nextChapterId
      fetchChapterDetails()
      fetchChapterPages()
      updateChapterNavState()
    } else if (hasNextChapterListPage) {
      fetchChapterListNextPage()
    }
  }

  private fun fetchChapterListFirstPage() {
    if (isFetchingChapterList) return
    if (mangaId == null || chapterLanguage == null) return

    viewModelScope.launch {
      isFetchingChapterList = true

      val chapterListResult = getChapterListUseCase(
        mangaId = mangaId!!,
        translatedLanguage = chapterLanguage!!,
        volumeOrder = ASC_ORDER,
        chapterOrder = ASC_ORDER,
      )
      chapterListResult
        .onSuccess { chapterList ->
          isFetchingChapterList = false
          currentChapterList = chapterList
          hasNextChapterListPage = currentChapterList.size >= CHAPTER_LIST_PER_PAGE_SIZE
          updateChapterNavState()
        }
        .onFailure {
          isFetchingChapterList = false
          currentChapterList = emptyList()
          hasNextChapterListPage = false
          Log.d(
            TAG,
            "fetchChapterListFirstPage have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  private fun fetchChapterListNextPage() {
    if (!hasNextChapterListPage || isFetchingChapterList) return
    if (mangaId == null || chapterLanguage == null) return

    viewModelScope.launch {
      isFetchingChapterList = true

      val chapterListResult = getChapterListUseCase(
        mangaId = mangaId!!,
        offset = currentChapterList.size,
        translatedLanguage = chapterLanguage!!,
        volumeOrder = ASC_ORDER,
        chapterOrder = ASC_ORDER,
      )
      chapterListResult
        .onSuccess { nextChapterList ->
          isFetchingChapterList = false
          currentChapterList += nextChapterList
          hasNextChapterListPage = nextChapterList.size >= CHAPTER_LIST_PER_PAGE_SIZE
          updateChapterNavState()
        }
        .onFailure {
          isFetchingChapterList = false
          hasNextChapterListPage = false
          Log.d(
            TAG,
            "fetchChapterListNextPage have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  private fun updateChapterNavState() {
    viewModelScope.launch {
      val currentChapterIndex = currentChapterList.indexOfFirst { it.id == currentChapterId }
      if (currentChapterIndex == -1) {
        _chapterNavState.update { it.copy(canNavigateNext = hasNextChapterListPage) }
        if (hasNextChapterListPage) fetchChapterListNextPage()
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

      prefetchChapterListNextPage(currentChapterIndex)
    }
  }

  private fun prefetchChapterListNextPage(currentChapterIndex: Int) {
    val isNearedLastChapter =
      currentChapterIndex == (currentChapterList.lastIndex - NEARED_LAST_CHAPTER_COUNT)
    if (isNearedLastChapter && hasNextChapterListPage) fetchChapterListNextPage()
  }

  fun updateChapterPage(newChapterPage: Int) {
    _chapterPagesUiState.update {
      (it as? ChapterPagesUiState.Success)?.copy(
        currentChapterPage = newChapterPage
      ) ?: it
    }

    // TODO: save history to a persistent store (e.g., Firebase, local database)
    // This would typically involve:
    // - mangaId
    // - currentChapterId
    // - newChapterPage
    // - timestamp
    // viewModelScope.launch { saveReadingProgressUseCase(...) }
  }

  fun retry() {
    fetchChapterDetails()
    fetchChapterPages()
  }

  companion object {
    private const val TAG = "ReaderViewModel"
    private const val CHAPTER_LIST_PER_PAGE_SIZE = 20
    private const val ASC_ORDER = "asc"
    private const val NEARED_LAST_CHAPTER_COUNT = 5
    private const val CACHE_EXPIRY_DURATION_MILLIS = 24 * 60 * 60 * 1000L // 24h
  }
}