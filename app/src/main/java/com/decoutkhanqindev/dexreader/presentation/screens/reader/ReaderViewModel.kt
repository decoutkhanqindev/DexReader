package com.decoutkhanqindev.dexreader.presentation.screens.reader

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.exception.FavoritesHistoryException
import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.MangaLanguage
import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.usecase.cache.AddChapterCacheUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.cache.ClearExpiredCacheUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.cache.GetChapterCacheUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.chapter.GetChapterDetailsUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.chapter.GetChapterListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.chapter.GetChapterPagesUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.history.AddAndUpdateToHistoryUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.history.ObserveHistoryUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetMangaDetailsUseCase
import com.decoutkhanqindev.dexreader.presentation.navigation.NavDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
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
  private val observeHistoryUseCase: ObserveHistoryUseCase,
  private val addAndUpdateToHistoryUseCase: AddAndUpdateToHistoryUseCase,
) : ViewModel() {
  private val chapterIdFromArg: String =
    checkNotNull(savedStateHandle[NavDestination.ReaderDestination.CHAPTER_ID_ARG])
  private val lastReadPageFromArg: Int =
    savedStateHandle[NavDestination.ReaderDestination.LAST_READ_PAGE_ARG] ?: 0
  private val mangaIdFromArg: String =
    checkNotNull(savedStateHandle[NavDestination.ReaderDestination.MANGA_ID_ARG])

  private var currentChapterId: String = chapterIdFromArg

  private val _chapterDetailsUiState = MutableStateFlow(ChapterDetailsUiState())
  val chapterDetailsUiState: StateFlow<ChapterDetailsUiState> = _chapterDetailsUiState.asStateFlow()

  private val _chapterPagesUiState =
    MutableStateFlow<ChapterPagesUiState>(ChapterPagesUiState.Loading)
  val chapterPagesUiState: StateFlow<ChapterPagesUiState> = _chapterPagesUiState.asStateFlow()

  private val _chapterNavUiState =
    MutableStateFlow(ChapterNavigationUiState(currentChapterId = currentChapterId))
  val chapterNavUiState: StateFlow<ChapterNavigationUiState> = _chapterNavUiState.asStateFlow()

  private val _userId = MutableStateFlow<String?>(null)

  private var mangaTitle: String? = null
  private var mangaCoverUrl: String? = null
  private var chapterLanguage: MangaLanguage? = null

  private var currentChapterList: List<Chapter> = emptyList()
  private var hasNextChapterListPage = true
  private var isFetchingChapterList = false

  private var currentReadingHistoryList: List<ReadingHistory> = emptyList()
  private var hasNextReadingHistoryListPage = true
  private var isObservingReadingHistoryList = false
  private var currentReadingHistory: ReadingHistory? = null
  private var observeHistoryJob: Job? = null

  private var _isFetchChapterDetailsDone = MutableStateFlow(false)
  private var _isFetchMangaDetailsDone = MutableStateFlow(false)
  private var _isObserveHistoryDone = MutableStateFlow(false)

  init {
    clearExpiredCache()
    observeHistoryFirstPage()
    fetchChapterDetails()
    observeIsFetchDataDone()
  }

  private fun observeIsFetchDataDone() {
    viewModelScope.launch {
      combine(
        _isFetchChapterDetailsDone,
        _isFetchMangaDetailsDone,
        _isObserveHistoryDone,
      ) { chapterDetailsDone, mangaDetailsDone, historyDone ->
        chapterDetailsDone && mangaDetailsDone && historyDone
      }.collectLatest { isDataDone ->
        if (isDataDone && _chapterPagesUiState.value is ChapterPagesUiState.Loading) {
          delay(DELAY_TIME_MILLIS)
          fetchChapterPages()
        }
      }
    }
  }

  private fun fetchChapterDetails() {
    viewModelScope.launch {
      getChapterDetailsUseCase(chapterId = currentChapterId)
        .onSuccess { chapter ->
          _chapterDetailsUiState.update {
            it.copy(
              volume = chapter.volume,
              chapterNumber = chapter.number,
              title = chapter.title
            )
          }

          chapterLanguage = chapter.language

          if (currentChapterList.isEmpty() && chapterLanguage != null)
            fetchChapterListFirstPage()

          if (mangaTitle == null && mangaCoverUrl == null)
            fetchMangaDetails()

          _isFetchChapterDetailsDone.value = true
        }
        .onFailure {
          _chapterDetailsUiState.update { ChapterDetailsUiState() }
          _isFetchChapterDetailsDone.value = true
          Log.e(TAG, "fetchChapterDetails have error: ${it.stackTraceToString()}")
        }
    }
  }

  private fun fetchMangaDetails() {
    viewModelScope.launch {
      getMangaDetailsUseCase(mangaId = mangaIdFromArg)
        .onSuccess { manga ->
          mangaTitle = manga.title
          mangaCoverUrl = manga.coverUrl
          _isFetchMangaDetailsDone.value = true
        }
        .onFailure {
          mangaTitle = null
          mangaCoverUrl = null
          _isFetchMangaDetailsDone.value = true
        }
    }
  }

  private fun fetchChapterPages(isPrefetch: Boolean = false) {
    val chapterIdToFetch =
      if (isPrefetch) _chapterNavUiState.value.nextChapterId ?: return
      else currentChapterId

    if (chapterIdToFetch.isBlank()) return

    viewModelScope.launch {
      if (!isPrefetch) _chapterPagesUiState.value = ChapterPagesUiState.Loading

      // from cache
      getChapterCacheUseCase(chapterId = chapterIdToFetch)
        .onSuccess { chapterPages ->
          if (!isPrefetch) {
            val initialChapterPage = ReadingHistory.findInitialPage(
              chapterId = chapterIdToFetch,
              navChapterId = chapterIdFromArg,
              navPage = lastReadPageFromArg,
              historyList = currentReadingHistoryList,
            )
            _chapterPagesUiState.value = ChapterPagesUiState.Success(
              chapterPages = chapterPages,
              currentChapterPage = initialChapterPage
            )
            prefetchNextChapterPages()
          }
          return@launch
        }
        .onFailure {
          Log.d(TAG, "getChapterCacheUseCase have error: ${it.stackTraceToString()}")
        }

      // from network
      getChapterPagesUseCase(chapterId = chapterIdToFetch)
        .onSuccess { chapterPages ->
          if (!isPrefetch) {
            val initialChapterPage = ReadingHistory.findInitialPage(
              chapterId = chapterIdToFetch,
              navChapterId = chapterIdFromArg,
              navPage = lastReadPageFromArg,
              historyList = currentReadingHistoryList,
            )
            _chapterPagesUiState.value = ChapterPagesUiState.Success(
              chapterPages = chapterPages,
              currentChapterPage = initialChapterPage
            )
          }

          addChapterCacheUseCase(mangaId = mangaIdFromArg, chapterPages = chapterPages)
            .onSuccess { Log.d(TAG, "addChapterCacheUseCase success") }
            .onFailure {
              Log.d(TAG, "addChapterCacheUseCase have error: ${it.stackTraceToString()}")
            }

          if (!isPrefetch) prefetchNextChapterPages()
        }
        .onFailure {
          if (!isPrefetch)
            _chapterPagesUiState.value = ChapterPagesUiState.Error
          Log.d(TAG, "getChapterPagesUseCase have error: ${it.stackTraceToString()}")
        }
    }
  }

  private fun prefetchNextChapterPages() {
    _chapterNavUiState.value.nextChapterId?.let { nextId ->
      if (nextId.isNotBlank()) fetchChapterPages(isPrefetch = true)
    }
  }

  private fun fetchChapterListFirstPage() {
    if (isFetchingChapterList || chapterLanguage == null) return

    viewModelScope.launch {
      isFetchingChapterList = true

      getChapterListUseCase(
        mangaId = mangaIdFromArg,
        language = chapterLanguage ?: MangaLanguage.ENGLISH,
        sortOrder = MangaSortOrder.ASC,
      )
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
          Log.d(TAG, "fetchChapterListFirstPage error: ${it.stackTraceToString()}")
        }
    }
  }

  private fun fetchChapterListNextPage() {
    if (!hasNextChapterListPage || isFetchingChapterList || chapterLanguage == null) return

    viewModelScope.launch {
      isFetchingChapterList = true
      getChapterListUseCase(
        mangaId = mangaIdFromArg,
        offset = currentChapterList.size,
        language = chapterLanguage ?: MangaLanguage.ENGLISH,
        sortOrder = MangaSortOrder.ASC,
      )
        .onSuccess { nextChapterList ->
          isFetchingChapterList = false
          currentChapterList += nextChapterList
          hasNextChapterListPage = nextChapterList.size >= CHAPTER_LIST_PER_PAGE_SIZE
          updateChapterNavState()
        }
        .onFailure {
          isFetchingChapterList = false
          hasNextChapterListPage = false
          Log.d(TAG, "fetchChapterListNextPage error: ${it.stackTraceToString()}")
        }
    }
  }

  private fun updateChapterNavState() {
    viewModelScope.launch {
      val currentChapterIndex = currentChapterList.indexOfFirst { it.id == currentChapterId }
      if (currentChapterIndex == -1) {
        _chapterNavUiState.update {
          it.copy(canNavigateNext = hasNextChapterListPage)
        }
        if (hasNextChapterListPage) fetchChapterListNextPage()
        return@launch
      }

      val isFirstChapter = currentChapterIndex == 0
      val isLastChapter = currentChapterIndex == currentChapterList.lastIndex

      _chapterNavUiState.update {
        it.copy(
          currentChapterId = currentChapterId,
          previousChapterId =
            if (!isFirstChapter) currentChapterList[currentChapterIndex - 1].id
            else null,
          nextChapterId =
            if (!isLastChapter) currentChapterList[currentChapterIndex + 1].id
            else null,
          canNavigatePrevious = !isFirstChapter,
          canNavigateNext = !isLastChapter || hasNextChapterListPage,
        )
      }

      prefetchChapterListNextPage(currentChapterIndex)
    }
  }

  private fun prefetchChapterListNextPage(currentChapterIndex: Int) {
    val isNearedLastChapter =
      currentChapterIndex >= (currentChapterList.lastIndex - NEARED_LAST_CHAPTER_COUNT)
    if (isNearedLastChapter && hasNextChapterListPage && currentChapterList.isNotEmpty())
      fetchChapterListNextPage()
  }

  fun navigateToPreviousChapter() {
    _chapterNavUiState.value.previousChapterId?.let { previousId ->
      currentChapterId = previousId
      updateChapterNavState()
      _chapterPagesUiState.value = ChapterPagesUiState.Loading
      _chapterDetailsUiState.value = ChapterDetailsUiState()
      _isFetchChapterDetailsDone.value = false
      updateCurrentReadingHistory()
      fetchChapterDetails()
    }
  }

  fun navigateToNextChapter() {
    _chapterNavUiState.value.nextChapterId?.let { nextId ->
      currentChapterId = nextId
      updateChapterNavState()
      _chapterPagesUiState.value = ChapterPagesUiState.Loading
      _chapterDetailsUiState.value = ChapterDetailsUiState()
      _isFetchChapterDetailsDone.value = false
      updateCurrentReadingHistory()
      fetchChapterDetails()
      if (hasNextChapterListPage) fetchChapterListNextPage()
      if (currentReadingHistory == null && hasNextReadingHistoryListPage) observeHistoryNextPage()
    }
  }

  fun updateChapterPage(chapterPage: Int, isFromHistory: Boolean = false) {
    val currentUiState = _chapterPagesUiState.value
    if (currentUiState is ChapterPagesUiState.Success) {
      if (currentUiState.currentChapterPage == chapterPage) return
      _chapterPagesUiState.value = currentUiState.copy(currentChapterPage = chapterPage)
      if (!isFromHistory) addAndUpdateToHistory()
    }
  }

  fun updateUserId(userId: String?) {
    if (_userId.value == userId) return
    _userId.value = userId
  }

  private fun addAndUpdateToHistory() {
    if (!_isFetchChapterDetailsDone.value || !_isFetchMangaDetailsDone.value) return

    val currentChapterDetailsState = _chapterDetailsUiState.value
    val currentChapterPagesState = _chapterPagesUiState.value

    if (mangaTitle == null || mangaCoverUrl == null ||
      currentChapterDetailsState == ChapterDetailsUiState() ||
      currentChapterPagesState !is ChapterPagesUiState.Success
    ) return

    viewModelScope.launch {
      _userId.value?.let { userId ->
        val newReadingHistory = ReadingHistory(
          id = ReadingHistory.generateId(mangaIdFromArg, currentChapterId),
          mangaId = mangaIdFromArg,
          mangaTitle = mangaTitle!!,
          mangaCoverUrl = mangaCoverUrl!!,
          chapterId = currentChapterId,
          chapterTitle = currentChapterDetailsState.title,
          chapterNumber = currentChapterDetailsState.chapterNumber,
          chapterVolume = currentChapterDetailsState.volume,
          lastReadPage = currentChapterPagesState.currentChapterPage,
          pageCount = currentChapterPagesState.chapterPages.totalPages,
          lastReadAt = null
        )
        addAndUpdateToHistoryUseCase(userId = userId, readingHistory = newReadingHistory)
          .onSuccess { Log.d(TAG, "addAndUpdateToHistory success") }
          .onFailure {
            Log.d(TAG, "addAndUpdateToHistory error: ${it.stackTraceToString()}")
          }
      }
    }
  }

  private fun observeHistoryFirstPage() {
    if (isObservingReadingHistoryList) return

    cancelObserveHistoryJob()
    observeHistoryJob = viewModelScope.launch {
      _isObserveHistoryDone.value = false
      isObservingReadingHistoryList = true

      _userId.collectLatest { userId ->
        if (userId == null) {
          _isObserveHistoryDone.value = true
          cancelObserveHistoryJob()
          return@collectLatest
        }

        observeHistoryUseCase(
          userId = userId,
          mangaId = mangaIdFromArg,
          limit = READING_HISTORY_LIST_PER_PAGE_SIZE,
        ).collect { result ->
          result
            .onSuccess { readingHistoryList ->
              isObservingReadingHistoryList = false
              currentReadingHistoryList = readingHistoryList
              hasNextReadingHistoryListPage =
                readingHistoryList.size >= READING_HISTORY_LIST_PER_PAGE_SIZE
              updateCurrentReadingHistory(isFromHistory = true)
            }
            .onFailure { throwable ->
              isObservingReadingHistoryList = false

              if (throwable is FavoritesHistoryException.PermissionDenied && _userId.value == null)
                return@onFailure

              currentReadingHistoryList = emptyList()
              hasNextReadingHistoryListPage = false
              _isObserveHistoryDone.value = true
              Log.d(TAG, "observeHistoryFirstPage have error: ${throwable.stackTraceToString()}")
            }
        }
      }
    }
  }

  private fun observeHistoryNextPage() {
    if (!hasNextReadingHistoryListPage || isObservingReadingHistoryList) return

    observeHistoryJob = viewModelScope.launch {
      val previousState = _isObserveHistoryDone.value
      _isObserveHistoryDone.value = false
      isObservingReadingHistoryList = true

      _userId.collectLatest { userId ->
        if (userId == null) {
          _isObserveHistoryDone.value = previousState
          cancelObserveHistoryJob()
          return@collectLatest
        }

        val lastReadingHistoryId = currentReadingHistoryList.lastOrNull()?.id

        observeHistoryUseCase(
          userId = userId,
          limit = READING_HISTORY_LIST_PER_PAGE_SIZE,
          mangaId = mangaIdFromArg,
          lastReadingHistoryId = lastReadingHistoryId
        ).collect { result ->
          result
            .onSuccess { readingHistoryList ->
              isObservingReadingHistoryList = false
              currentReadingHistoryList += readingHistoryList
              hasNextReadingHistoryListPage =
                readingHistoryList.size >= READING_HISTORY_LIST_PER_PAGE_SIZE
              updateCurrentReadingHistory(isFromHistory = true)
            }
            .onFailure { throwable ->
              isObservingReadingHistoryList = false

              if (throwable is FavoritesHistoryException.PermissionDenied && _userId.value == null)
                return@onFailure

              hasNextReadingHistoryListPage = false
              _isObserveHistoryDone.value = previousState
              Log.d(TAG, "observeHistoryNextPage have error: ${throwable.stackTraceToString()}")
            }
        }
      }
    }
  }

  private fun updateCurrentReadingHistory(isFromHistory: Boolean = false) {
    val readingHistory = currentReadingHistoryList.find { it.chapterId == currentChapterId }

    when {
      readingHistory != null -> {
        currentReadingHistory = readingHistory

        val currentUiState = _chapterPagesUiState.value
        if (currentUiState is ChapterPagesUiState.Success &&
          currentUiState.currentChapterPage != readingHistory.lastReadPage
        ) {
          viewModelScope.launch {
            delay(DELAY_TIME_MILLIS)
            updateChapterPage(
              chapterPage = readingHistory.lastReadPage,
              isFromHistory = isFromHistory
            )
            _isObserveHistoryDone.value = true
          }
        } else _isObserveHistoryDone.value = true
      }

      else -> {
        if (hasNextReadingHistoryListPage) observeHistoryNextPage()
        else {
          currentReadingHistory = null
          _isObserveHistoryDone.value = true
        }
      }
    }
  }

  private fun clearExpiredCache() {
    viewModelScope.launch {
      clearExpiredCacheUseCase()
        .onSuccess { Log.d(TAG, "clearExpiredCache success.") }
        .onFailure {
          Log.d(TAG, "clearExpiredCache error: ${it.stackTraceToString()}")
        }
    }
  }

  fun retry() {
    if (_chapterPagesUiState.value is ChapterPagesUiState.Error) {
      _isFetchChapterDetailsDone.value = false
      _isFetchMangaDetailsDone.value = false
      _isObserveHistoryDone.value = false
      observeHistoryFirstPage()
      fetchChapterDetails()
    }
  }

  private fun cancelObserveHistoryJob() {
    observeHistoryJob?.cancel()
    observeHistoryJob = null
  }

  override fun onCleared() {
    cancelObserveHistoryJob()
    super.onCleared()
  }

  companion object {
    private const val TAG = "ReaderViewModel"
    private const val CHAPTER_LIST_PER_PAGE_SIZE = 20
    private const val READING_HISTORY_LIST_PER_PAGE_SIZE = 50
    private const val NEARED_LAST_CHAPTER_COUNT = 5
    private const val DELAY_TIME_MILLIS = 100L
  }
}
