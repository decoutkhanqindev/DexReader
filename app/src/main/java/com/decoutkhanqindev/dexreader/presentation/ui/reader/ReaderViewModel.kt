package com.decoutkhanqindev.dexreader.presentation.ui.reader

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.domain.usecase.cache.AddChapterCacheUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.cache.ClearExpiredCacheUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.cache.GetChapterCacheUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.chapter.GetChapterDetailsUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.chapter.GetChapterListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.chapter.GetChapterPagesUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.history.AddAndUpdateToHistoryUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetMangaDetailsUseCase
import com.decoutkhanqindev.dexreader.presentation.navigation.NavDestination
import com.decoutkhanqindev.dexreader.utils.toLanguageCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
  private val clearExpiredCacheUseCase: ClearExpiredCacheUseCase,
  private val getMangaDetailsUseCase: GetMangaDetailsUseCase,
  private val addAndUpdateToHistoryUseCase: AddAndUpdateToHistoryUseCase
) : ViewModel() {
  private val chapterIdFromArg: String =
    checkNotNull(savedStateHandle[NavDestination.ReaderDestination.CHAPTER_ID_ARG])
  private var currentChapterId: String = chapterIdFromArg

  private val _chapterDetailsUiState =
    MutableStateFlow<ChapterDetailsUiState>(ChapterDetailsUiState())
  val chapterDetailsUiState: StateFlow<ChapterDetailsUiState> = _chapterDetailsUiState.asStateFlow()

  private val _chapterPagesUiState =
    MutableStateFlow<ChapterPagesUiState>(ChapterPagesUiState.Loading)
  val chapterPagesUiState: StateFlow<ChapterPagesUiState> = _chapterPagesUiState.asStateFlow()

  private val _chapterNavState =
    MutableStateFlow(ChapterNavigationState(currentChapterId = currentChapterId))
  val chapterNavState: StateFlow<ChapterNavigationState> = _chapterNavState.asStateFlow()

  private var mangaId: String? = null
  private var mangaTitle: String? = null
  private var mangaCoverUrl: String? = null
  private var chapterLanguage: String? = null
  private var currentChapterList: List<Chapter> = emptyList()
  private var hasNextChapterListPage = true
  private var isFetchingChapterList = false

  private val _userId = MutableStateFlow<String?>(null)
  private val userId: StateFlow<String?> = _userId.asStateFlow()

  private var addToHistoryJob: Job? = null

  init {
    clearExpiredCache()
    fetchChapterDetails()
    fetchChapterPages()
  }

  private fun clearExpiredCache() {
    viewModelScope.launch {
      val expiryTimestamp = System.currentTimeMillis() - CACHE_EXPIRY_TIME_MILLIS
      val clearExpiredCacheResult = clearExpiredCacheUseCase(expiryTimestamp)
      clearExpiredCacheResult
        .onSuccess { Log.d(TAG, "clearExpiredCache is success.") }
        .onFailure {
          Log.d(TAG, "clearExpiredCache have error: ${it.stackTraceToString()}")
        }
    }
  }

  private fun fetchChapterDetails() {
    viewModelScope.launch {
      val chapterDetailsResult = getChapterDetailsUseCase(chapterId = currentChapterId)
      chapterDetailsResult
        .onSuccess { chapter ->
          _chapterDetailsUiState.update {
            it.copy(
              volume = chapter.volume,
              chapterNumber = chapter.chapterNumber,
              title = chapter.title
            )
          }

          mangaId = chapter.mangaId
          chapterLanguage = chapter.translatedLanguage.toLanguageCode()

          if (currentChapterList.isEmpty() &&
            mangaId != null && chapterLanguage != null
          ) fetchChapterListFirstPage()

          if (mangaId != null) fetchMangaDetails()
        }
        .onFailure {
          _chapterDetailsUiState.update { ChapterDetailsUiState() }
          Log.e(
            TAG,
            "fetchChapterDetails have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  private fun fetchMangaDetails() {
    if (mangaId == null &&
      _chapterPagesUiState.value !is ChapterPagesUiState.Success
    ) return

    viewModelScope.launch {
      val getMangaDetailsResult = getMangaDetailsUseCase(mangaId = mangaId!!)
      getMangaDetailsResult
        .onSuccess { manga ->
          mangaTitle = manga.title
          mangaCoverUrl = manga.coverUrl
        }
        .onFailure {
          mangaTitle = null
          mangaCoverUrl = null
        }
    }
  }

  private fun fetchChapterPages(isPrefetch: Boolean = false) {
    val chapterIdToFetch =
      if (isPrefetch) _chapterNavState.value.nextChapterId ?: return
      else currentChapterId

    if (chapterIdToFetch.isBlank()) return

    viewModelScope.launch {
      if (!isPrefetch) _chapterPagesUiState.value = ChapterPagesUiState.Loading

      val cachedResult = getChapterCacheUseCase(chapterId = chapterIdToFetch)
      cachedResult
        .onSuccess { chapterPages ->
          if (!isPrefetch) {
            _chapterPagesUiState.value = ChapterPagesUiState.Success(chapterPages = chapterPages)
            prefetchNextChapterPages()
          }
          return@launch
        }
        .onFailure {
          Log.d(TAG, "getChapterCacheUseCase have error: ${it.stackTraceToString()}")
        }

      val networkResult = getChapterPagesUseCase(chapterId = chapterIdToFetch)
      networkResult
        .onSuccess { chapterPages ->
          if (!isPrefetch)
            _chapterPagesUiState.value = ChapterPagesUiState.Success(chapterPages = chapterPages)

          if (mangaId != null) {
            addChapterCacheUseCase(mangaId = mangaId!!, chapterPages = chapterPages)
              .onSuccess {
                Log.d(TAG, "addChapterCacheUseCase success: $chapterIdToFetch")
              }
              .onFailure {
                Log.d(TAG, "addChapterCacheUseCase have error: ${it.stackTraceToString()}")
              }
          }

          if (!isPrefetch) prefetchNextChapterPages()
        }
        .onFailure {
          if (!isPrefetch) _chapterPagesUiState.value = ChapterPagesUiState.Error
          Log.d(TAG, "getChapterPagesUseCase have error: ${it.stackTraceToString()}")
        }
    }
  }

  private fun prefetchNextChapterPages() {
    val currentChapterNavState = _chapterNavState.value
    currentChapterNavState.nextChapterId?.let { nextId ->
      if (nextId.isNotBlank()) {
        if (mangaId == null) return
        fetchChapterPages(isPrefetch = true)
      }
    }
  }

  fun navigateToPreviousChapter() {
    val currentChapterNavState = _chapterNavState.value
    currentChapterNavState.previousChapterId?.let { previousId ->
      currentChapterId = previousId
      fetchChapterDetails()
      fetchChapterPages()
      updateChapterNavState()
    }
  }

  fun navigateToNextChapter() {
    val currentChapterNavState = _chapterNavState.value
    currentChapterNavState.nextChapterId?.let { nextId ->
      currentChapterId = nextId
      fetchChapterDetails()
      fetchChapterPages()
      updateChapterNavState()
      if (hasNextChapterListPage) fetchChapterListNextPage()
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
        _chapterNavState.update {
          it.copy(canNavigateNext = hasNextChapterListPage)
        }
        if (hasNextChapterListPage) fetchChapterListNextPage()
        return@launch
      }

      val isFirstChapter = currentChapterIndex == 0
      val isLastChapter = currentChapterIndex == currentChapterList.lastIndex

      _chapterNavState.update {
        it.copy(
          currentChapterId = currentChapterId,
          previousChapterId =
            if (!isFirstChapter) currentChapterList[currentChapterIndex - 1].id
            else null,
          nextChapterId =
            if (!isLastChapter) currentChapterList[currentChapterIndex + 1].id
            else null,
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

  fun updateUserId(userId: String) {
    if (_userId.value == userId) return
    _userId.value = userId
  }

  fun updateChapterPage(newChapterPage: Int) {
    val currentChapterPagesUiState = _chapterPagesUiState.value
    if (currentChapterPagesUiState is ChapterPagesUiState.Success) {
      if (currentChapterPagesUiState.currentChapterPage == newChapterPage) return
      _chapterPagesUiState.value =
        currentChapterPagesUiState.copy(currentChapterPage = newChapterPage)
      addToHistory()
    }
  }

  private fun addToHistory() {
    val currentChapterDetailsUiState = _chapterDetailsUiState.value
    val currentChapterPagesUiState = _chapterPagesUiState.value
    if (mangaId == null ||
      mangaTitle == null ||
      mangaCoverUrl == null ||
      currentChapterPagesUiState !is ChapterPagesUiState.Success ||
      currentChapterDetailsUiState == ChapterDetailsUiState()
    ) return

    cancelAddToHistoryJob()
    addToHistoryJob = viewModelScope.launch {
      delay(DELAY_TIME_MILLIS)

      userId.value?.let { userId ->
        val newId = "${mangaId}_${currentChapterId}"
        val newReadingHistory = ReadingHistory(
          id = newId,
          mangaId = mangaId!!,
          mangaTitle = mangaTitle!!,
          mangaCoverUrl = mangaCoverUrl!!,
          chapterId = currentChapterId,
          chapterTitle = currentChapterDetailsUiState.title,
          chapterNumber = currentChapterDetailsUiState.chapterNumber,
          chapterVolume = currentChapterDetailsUiState.volume,
          lastReadPage = currentChapterPagesUiState.currentChapterPage,
          totalChapterPages = currentChapterPagesUiState.chapterPages.totalPages,
          lastReadAt = null
        )

        val addToHistoryResult = addAndUpdateToHistoryUseCase(
          userId = userId,
          readingHistory = newReadingHistory
        )
        addToHistoryResult
          .onSuccess { Log.d(TAG, "addToHistory success") }
          .onFailure {
            Log.d(TAG, "addToHistory have error: ${it.stackTraceToString()}")
          }
      }
    }
  }

  fun retry() {
    if (_chapterDetailsUiState.value == ChapterDetailsUiState())
      fetchChapterDetails()
    if (_chapterPagesUiState.value is ChapterPagesUiState.Error)
      fetchChapterPages()
  }

  fun cancelAddToHistoryJob() {
    addToHistoryJob?.cancel()
    addToHistoryJob = null
  }

  override fun onCleared() {
    super.onCleared()
    cancelAddToHistoryJob()
  }

  companion object {
    private const val TAG = "ReaderViewModel"
    private const val CHAPTER_LIST_PER_PAGE_SIZE = 20
    private const val ASC_ORDER = "asc"
    private const val NEARED_LAST_CHAPTER_COUNT = 5
    private const val CACHE_EXPIRY_TIME_MILLIS = 24 * 60 * 60 * 1000L // 24h
    private const val DELAY_TIME_MILLIS = 500L
  }
}