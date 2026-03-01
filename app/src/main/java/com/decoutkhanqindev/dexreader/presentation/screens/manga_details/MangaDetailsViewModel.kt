package com.decoutkhanqindev.dexreader.presentation.screens.manga_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.FavoriteManga
import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.domain.model.criteria.sort.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.usecase.chapter.GetChapterListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.favorites.AddToFavoritesUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.favorites.ObserveIsFavoriteUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.favorites.RemoveFromFavoritesUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.history.ObserveHistoryUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetMangaDetailsUseCase
import com.decoutkhanqindev.dexreader.presentation.mapper.LanguageMapper.toMangaLanguage
import com.decoutkhanqindev.dexreader.presentation.mapper.LanguageMapper.toMangaLanguageName
import com.decoutkhanqindev.dexreader.presentation.model.MangaLanguageName
import com.decoutkhanqindev.dexreader.presentation.navigation.NavDestination
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BasePaginationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaDetailsViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val getMangaDetailsUseCase: GetMangaDetailsUseCase,
  private val getChapterListUseCase: GetChapterListUseCase,
  private val addToFavoritesUseCase: AddToFavoritesUseCase,
  private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
  private val observeIsFavoriteUseCase: ObserveIsFavoriteUseCase,
  private val observeHistoryUseCase: ObserveHistoryUseCase,
) : ViewModel() {
  private val mangaIdFromArg: String =
    checkNotNull(savedStateHandle[NavDestination.MangaDetailsDestination.MANGA_ID_ARG])

  private val _mangaDetailsUiState =
    MutableStateFlow<MangaDetailsUiState>(MangaDetailsUiState.Loading)
  val mangaDetailsUiState: StateFlow<MangaDetailsUiState> = _mangaDetailsUiState.asStateFlow()

  private val _mangaChaptersUiState =
    MutableStateFlow<BasePaginationUiState<Chapter>>(BasePaginationUiState.FirstPageLoading)
  val mangaChaptersUiState: StateFlow<BasePaginationUiState<Chapter>> =
    _mangaChaptersUiState.asStateFlow()

  private val _chapterLanguage = MutableStateFlow(MangaLanguageName.ENGLISH)
  val chapterLanguage: StateFlow<MangaLanguageName> = _chapterLanguage.asStateFlow()

  val availableLanguages: StateFlow<List<MangaLanguageName>> = _mangaDetailsUiState
    .map { state ->
      if (state is MangaDetailsUiState.Success)
        state.manga.availableTranslatedLanguages.map { it.toMangaLanguageName() }
      else emptyList()
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
      initialValue = emptyList()
    )

  private val _userId = MutableStateFlow<String?>(null)

  private val _isFavorite = MutableStateFlow(false)
  val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()
  private var observeIsFavoriteJob: Job? = null

  private val _readingHistoryList = MutableStateFlow<List<ReadingHistory>>(emptyList())
  val readingHistoryList: StateFlow<List<ReadingHistory>> = _readingHistoryList.asStateFlow()
  private var hasNextReadingHistoryListPage = true
  private var isObservingReadingHistoryList = false
  private var observeHistoryJob: Job? = null

  private val _startedChapter = MutableStateFlow<Chapter?>(null)
  val startedChapter: StateFlow<Chapter?> = _startedChapter.asStateFlow()
  val continueChapter: StateFlow<ReadingHistory?> = _readingHistoryList
    .map { historyList ->
      historyList.firstOrNull { it.lastReadPage < it.totalChapterPages - 1 } // chapter is not finish reading
        ?: historyList.firstOrNull() // all have been read, get the most recently read chapter
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
      initialValue = null
    )

  init {
    observeIsFavorite()
    fetchMangaDetails()
    fetchFirstChapter()
    fetchChapterListFirstPage()
    observeHistoryFirstPage()
  }

  private fun fetchMangaDetails() {
    viewModelScope.launch {
      getMangaDetailsUseCase(mangaId = mangaIdFromArg)
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
      getChapterListUseCase(
        mangaId = mangaIdFromArg,
        limit = 1,
        language = _chapterLanguage.value.toMangaLanguage(),
        sortOrder = MangaSortOrder.ASC,
      )
        .onSuccess {
          if (it.isNotEmpty()) _startedChapter.value = it.first()
          else _startedChapter.value = null
        }
        .onFailure {
          _startedChapter.value = null
          Log.d(TAG, "fetchFirstChapter have error: ${it.stackTraceToString()}")
        }
    }
  }

  private fun fetchChapterListFirstPage() {
    viewModelScope.launch {
      _mangaChaptersUiState.value = BasePaginationUiState.FirstPageLoading

      getChapterListUseCase(
        mangaId = mangaIdFromArg,
        language = _chapterLanguage.value.toMangaLanguage(),
      )
        .onSuccess { chapterList ->
          val hasNextPage = chapterList.size >= CHAPTER_LIST_PER_PAGE_SIZE
          _mangaChaptersUiState.value = BasePaginationUiState.Content(
            currentList = chapterList,
            currentPage = FIRST_PAGE,
            nextPageState =
              if (!hasNextPage) BaseNextPageState.NO_MORE_ITEMS
              else BaseNextPageState.IDLE
          )
        }
        .onFailure {
          _mangaChaptersUiState.value = BasePaginationUiState.FirstPageError
          Log.d(TAG, "fetchChapterListFirstPage have error: ${it.stackTraceToString()}")
        }
    }
  }

  fun fetchChapterListNextPage() {
    when (val currentMangaChaptersUiState = _mangaChaptersUiState.value) {
      BasePaginationUiState.FirstPageLoading,
      BasePaginationUiState.FirstPageError,
        -> return

      is BasePaginationUiState.Content -> {
        when (currentMangaChaptersUiState.nextPageState) {
          BaseNextPageState.LOADING,
          BaseNextPageState.NO_MORE_ITEMS,
            -> return

          BaseNextPageState.ERROR -> retryFetchChapterListNextPage()

          BaseNextPageState.IDLE ->
            fetchChapterListNextPageInternal(currentMangaChaptersUiState)
        }
      }
    }
  }

  private fun fetchChapterListNextPageInternal(currentMangaChaptersUiState: BasePaginationUiState.Content<Chapter>) {
    viewModelScope.launch {
      _mangaChaptersUiState.value =
        currentMangaChaptersUiState.copy(nextPageState = BaseNextPageState.LOADING)

      val currentMangaList = currentMangaChaptersUiState.currentList
      val nextPage: Int = currentMangaChaptersUiState.currentPage + 1

      getChapterListUseCase(
        mangaId = mangaIdFromArg,
        offset = currentMangaList.size,
        language = _chapterLanguage.value.toMangaLanguage(),
      )
        .onSuccess { nextChapterList ->
          val updatedChapterList = currentMangaList + nextChapterList
          val hasNextPage = nextChapterList.size >= CHAPTER_LIST_PER_PAGE_SIZE
          _mangaChaptersUiState.value = currentMangaChaptersUiState.copy(
            currentList = updatedChapterList,
            currentPage = nextPage,
            nextPageState =
              if (!hasNextPage) BaseNextPageState.NO_MORE_ITEMS
              else BaseNextPageState.IDLE
          )
        }
        .onFailure {
          _mangaChaptersUiState.value =
            currentMangaChaptersUiState.copy(nextPageState = BaseNextPageState.ERROR)
          Log.d(TAG, "fetchChapterListNextPageInternal have error: ${it.stackTraceToString()}")
        }
    }
  }

  private fun observeIsFavorite() {
    cancelObserveIsFavoriteJob()
    observeIsFavoriteJob = viewModelScope.launch {
      _mangaDetailsUiState.collect { currentUiState ->
        if (currentUiState !is MangaDetailsUiState.Success) return@collect
        val mangaId = currentUiState.manga.id

        _userId.collectLatest { userId ->
          if (userId == null) {
            _isFavorite.value = false
            cancelObserveIsFavoriteJob()
            return@collectLatest
          }

          observeIsFavoriteUseCase(
            userId = userId,
            mangaId = mangaId
          ).collect { result ->
            result
              .onSuccess { _isFavorite.value = it }
              .onFailure { throwable ->
                _isFavorite.value = false

                if (throwable.message?.contains(PERMISSION_DENIED_EXCEPTION) == true && _userId.value == null)
                  return@onFailure

                Log.d(TAG, "observeIsFavorite have error: ${throwable.stackTraceToString()}")
              }
          }
        }
      }
    }
  }

  fun addToFavorites() {
    val currentUiState = _mangaDetailsUiState.value
    if (currentUiState !is MangaDetailsUiState.Success) return

    viewModelScope.launch {
      val manga = currentUiState.manga
      _userId.value?.let { userId ->
        val newFavoriteManga = FavoriteManga(
          id = manga.id,
          title = manga.title,
          coverUrl = manga.coverUrl,
          author = manga.author,
          status = manga.status,
          addedAt = null
        )
        addToFavoritesUseCase(userId = userId, manga = newFavoriteManga)
          .onSuccess { Log.d(TAG, "adResponseFavorites success") }
          .onFailure {
            Log.d(TAG, "adResponseFavorites have error: ${it.stackTraceToString()}")
          }
      }
    }
  }

  fun removeFromFavorites() {
    val currentUiState = _mangaDetailsUiState.value
    if (currentUiState !is MangaDetailsUiState.Success) return

    viewModelScope.launch {
      val mangaId = currentUiState.manga.id
      _userId.value?.let { userId ->
        removeFromFavoritesUseCase(userId = userId, mangaId = mangaId)
          .onSuccess { Log.d(TAG, "removeFromFavorites success") }
          .onFailure {
            Log.d(TAG, "removeFromFavorites have error: ${it.stackTraceToString()}")
          }
      }
    }
  }

  private fun observeHistoryFirstPage() {
    if (isObservingReadingHistoryList) return

    cancelObserveHistoryJob()
    observeHistoryJob = viewModelScope.launch {
      isObservingReadingHistoryList = true

      _userId.collectLatest { userId ->
        if (userId == null) {
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
              _readingHistoryList.value = readingHistoryList
              hasNextReadingHistoryListPage =
                readingHistoryList.size >= READING_HISTORY_LIST_PER_PAGE_SIZE

              if (hasNextReadingHistoryListPage) observeHistoryNextPage()
              else return@onSuccess
            }
            .onFailure { throwable ->
              isObservingReadingHistoryList = false

              if (throwable.message?.contains(PERMISSION_DENIED_EXCEPTION) == true && _userId.value == null)
                return@onFailure

              _readingHistoryList.value = emptyList()
              hasNextReadingHistoryListPage = false
              Log.d(TAG, "observeHistoryFirstPage have error: ${throwable.stackTraceToString()}")
            }
        }
      }
    }
  }

  private fun observeHistoryNextPage() {
    if (!hasNextReadingHistoryListPage || isObservingReadingHistoryList) return

    observeHistoryJob = viewModelScope.launch {
      isObservingReadingHistoryList = true

      _userId.collectLatest { userId ->
        if (userId == null) {
          cancelObserveHistoryJob()
          return@collectLatest
        }

        val lastReadingHistoryId = _readingHistoryList.value.lastOrNull()?.id

        observeHistoryUseCase(
          userId = userId,
          limit = READING_HISTORY_LIST_PER_PAGE_SIZE,
          mangaId = mangaIdFromArg,
          lastReadingHistoryId = lastReadingHistoryId
        ).collect { result ->
          result
            .onSuccess { readingHistoryList ->
              isObservingReadingHistoryList = false
              _readingHistoryList.value += readingHistoryList
              hasNextReadingHistoryListPage =
                readingHistoryList.size >= READING_HISTORY_LIST_PER_PAGE_SIZE

              if (hasNextReadingHistoryListPage) observeHistoryNextPage()
              else return@onSuccess
            }
            .onFailure { throwable ->
              isObservingReadingHistoryList = false

              if (throwable.message?.contains(PERMISSION_DENIED_EXCEPTION) == true && _userId.value == null)
                return@onFailure

              hasNextReadingHistoryListPage = false
              Log.d(TAG, "observeHistoryNextPage have error: ${throwable.stackTraceToString()}")
            }
        }
      }
    }
  }

  fun updateUserId(id: String?) {
    if (_userId.value == id) return
    _userId.value = id
  }

  fun updateChapterLanguage(language: MangaLanguageName) {
    if (_chapterLanguage.value == language) return
    _chapterLanguage.value = language
    fetchFirstChapter()
    fetchChapterListFirstPage()
  }

  fun retry() {
    if (_mangaDetailsUiState.value is MangaDetailsUiState.Error) fetchMangaDetails()
    if (_startedChapter.value == null) fetchFirstChapter()
    if (_mangaChaptersUiState.value is BasePaginationUiState.FirstPageError) fetchChapterListFirstPage()
    observeIsFavorite()
    observeHistoryFirstPage()
  }

  fun retryFetchChapterListFirstPage() {
    if (_mangaChaptersUiState.value is BasePaginationUiState.FirstPageError)
      fetchChapterListFirstPage()
  }

  fun retryFetchChapterListNextPage() {
    val currentMangaChaptersUiState = _mangaChaptersUiState.value
    if (currentMangaChaptersUiState is BasePaginationUiState.Content &&
      currentMangaChaptersUiState.nextPageState == BaseNextPageState.ERROR
    ) fetchChapterListNextPageInternal(currentMangaChaptersUiState)
  }

  private fun cancelObserveIsFavoriteJob() {
    observeIsFavoriteJob?.cancel()
    observeIsFavoriteJob = null
  }

  private fun cancelObserveHistoryJob() {
    observeHistoryJob?.cancel()
    observeHistoryJob = null
  }

  override fun onCleared() {
    cancelObserveIsFavoriteJob()
    cancelObserveHistoryJob()
    super.onCleared()
  }

  companion object {
    private const val TAG = "MangaDetailsViewModel"
    private const val FIRST_PAGE = 1
    private const val CHAPTER_LIST_PER_PAGE_SIZE = 20
    private const val READING_HISTORY_LIST_PER_PAGE_SIZE = 50
    private const val PERMISSION_DENIED_EXCEPTION = "PERMISSION_DENIED"
    private const val STOP_TIMEOUT_MILLIS = 5000L
  }
}