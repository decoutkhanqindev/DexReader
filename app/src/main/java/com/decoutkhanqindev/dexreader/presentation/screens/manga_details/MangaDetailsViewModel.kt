package com.decoutkhanqindev.dexreader.presentation.screens.manga_details


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.value.criteria.MangaSortOrder
import com.decoutkhanqindev.dexreader.domain.entity.manga.Manga
import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingHistory
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetChapterListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetMangaDetailsUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.user.favorite.AddToFavoritesUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.user.favorite.ObserveIsFavoriteUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.user.favorite.RemoveFromFavoritesUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.user.history.ObserveHistoryUseCase
import com.decoutkhanqindev.dexreader.presentation.mapper.ChapterMapper.toChapterModel
import com.decoutkhanqindev.dexreader.presentation.mapper.LanguageMapper.toMangaLanguage
import com.decoutkhanqindev.dexreader.presentation.mapper.MangaMapper.toMangaModel
import com.decoutkhanqindev.dexreader.presentation.mapper.ReadingHistoryMapper.toReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.mapper.ErrorMapper.toFeatureError
import com.decoutkhanqindev.dexreader.presentation.model.manga.ChapterModel
import com.decoutkhanqindev.dexreader.presentation.value.manga.MangaLanguageValue
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.navigation.NavDestination
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BasePaginationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
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
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class MangaDetailsViewModel
@Inject
constructor(
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
    MutableStateFlow<BasePaginationUiState<ChapterModel>>(BasePaginationUiState.FirstPageLoading)
  val mangaChaptersUiState: StateFlow<BasePaginationUiState<ChapterModel>> =
    _mangaChaptersUiState.asStateFlow()

  private val _chapterLanguage = MutableStateFlow(MangaLanguageValue.ENGLISH)
  val chapterLanguage: StateFlow<MangaLanguageValue> = _chapterLanguage.asStateFlow()

  val availableLanguages: StateFlow<ImmutableList<MangaLanguageValue>> =
    _mangaDetailsUiState
      .map { state ->
        if (state is MangaDetailsUiState.Success)
          state.manga.availableLanguages
        else persistentListOf()
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = persistentListOf()
      )

  private val _userId = MutableStateFlow<String?>(null)

  private val _isFavorite = MutableStateFlow(false)
  val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()
  private var observeIsFavoriteJob: Job? = null

  private val _domainManga = MutableStateFlow<Manga?>(null)

  private val _readingHistoryList =
    MutableStateFlow<ImmutableList<ReadingHistory>>(persistentListOf())
  val readingHistoryList: StateFlow<ImmutableList<ReadingHistoryModel>> =
    _readingHistoryList
      .map { list -> list.map { it.toReadingHistoryModel() }.toPersistentList() }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = persistentListOf()
      )
  private var hasNextReadingHistoryListPage = true
  private var isObservingReadingHistoryList = false
  private var observeHistoryJob: Job? = null

  private val _startedChapterId = MutableStateFlow<String?>(null)
  val startedChapterId: StateFlow<String?> = _startedChapterId.asStateFlow()
  val continueChapter: StateFlow<ReadingHistoryModel?> =
    _readingHistoryList
      .map { ReadingHistory.findContinueTarget(it)?.toReadingHistoryModel() }
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
          _domainManga.value = it
          _mangaDetailsUiState.value = MangaDetailsUiState.Success(manga = it.toMangaModel())
        }
        .onFailure { throwable ->
          _mangaDetailsUiState.value = MangaDetailsUiState.Error(throwable.toFeatureError())
          Log.d(TAG, "fetchMangaDetails have error: ${throwable.stackTraceToString()}")
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
          if (it.isNotEmpty()) _startedChapterId.value = it.first().id
          else _startedChapterId.value = null
        }
        .onFailure {
          _startedChapterId.value = null
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
          _mangaChaptersUiState.value =
            BasePaginationUiState.Content(
              currentList = chapterList.map { it.toChapterModel() }.toPersistentList(),
              currentPage = FIRST_PAGE,
              nextPageState =
                BaseNextPageState.fromPageSize(
                  chapterList.size,
                  CHAPTER_LIST_PER_PAGE_SIZE
                )
            )
        }
        .onFailure { throwable ->
          _mangaChaptersUiState.value =
            BasePaginationUiState.FirstPageError(throwable.toFeatureError())
          Log.d(TAG, "fetchChapterListFirstPage have error: ${throwable.stackTraceToString()}")
        }
    }
  }

  fun fetchChapterListNextPage() {
    when (val currentMangaChaptersUiState = _mangaChaptersUiState.value) {
      BasePaginationUiState.FirstPageLoading, is BasePaginationUiState.FirstPageError -> return
      is BasePaginationUiState.Content -> {
        when (currentMangaChaptersUiState.nextPageState) {
          BaseNextPageState.LOADING, BaseNextPageState.NO_MORE_ITEMS -> return
          BaseNextPageState.ERROR -> retryFetchChapterListNextPage()
          BaseNextPageState.IDLE -> fetchChapterListNextPageInternal(currentMangaChaptersUiState)
        }
      }
    }
  }

  private fun fetchChapterListNextPageInternal(
    currentMangaChaptersUiState: BasePaginationUiState.Content<ChapterModel>,
  ) {
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
          val updatedChapterList = currentMangaList + nextChapterList.map { it.toChapterModel() }
          _mangaChaptersUiState.value =
            currentMangaChaptersUiState.copy(
              currentList = updatedChapterList.toPersistentList(),
              currentPage = nextPage,
              nextPageState =
                BaseNextPageState.fromPageSize(
                  nextChapterList.size,
                  CHAPTER_LIST_PER_PAGE_SIZE
                )
            )
        }
        .onFailure {
          _mangaChaptersUiState.value =
            currentMangaChaptersUiState.copy(nextPageState = BaseNextPageState.ERROR)
          Log.d(
            TAG,
            "fetchChapterListNextPageInternal have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  private fun observeIsFavorite() {
    cancelObserveIsFavoriteJob()
    observeIsFavoriteJob =
      viewModelScope.launch {
        _mangaDetailsUiState.collect { currentUiState ->
          if (currentUiState !is MangaDetailsUiState.Success) return@collect
          val mangaId = currentUiState.manga.id

          _userId.collectLatest { userId ->
            if (userId == null) {
              _isFavorite.value = false
              cancelObserveIsFavoriteJob()
              return@collectLatest
            }

            try {
              observeIsFavoriteUseCase(userId = userId, mangaId = mangaId).collect { result ->
                result.onSuccess { _isFavorite.value = it }.onFailure { throwable ->
                  _isFavorite.value = false

                  if (throwable is BusinessException.Resource.AccessDenied && _userId.value == null)
                    return@onFailure

                  Log.d(TAG, "observeIsFavorite have error: ${throwable.stackTraceToString()}")
                }
              }
            } catch (c: CancellationException) {
              throw c
            } catch (e: Exception) {
              _isFavorite.value = false
              Log.d(TAG, "observeIsFavorite have error: ${e.stackTraceToString()}")
            }
          }
        }
      }
  }

  fun addToFavorites() {
    val domainManga = _domainManga.value ?: return

    viewModelScope.launch {
      _userId.value?.let { userId ->
        addToFavoritesUseCase(userId = userId, manga = domainManga)
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
    observeHistoryJob =
      viewModelScope.launch {
        isObservingReadingHistoryList = true

        _userId.collectLatest { userId ->
          if (userId == null) {
            cancelObserveHistoryJob()
            return@collectLatest
          }

          try {
            observeHistoryUseCase(
              userId = userId,
              mangaId = mangaIdFromArg,
              limit = READING_HISTORY_LIST_PER_PAGE_SIZE,
            )
              .collect { result ->
                result
                  .onSuccess { readingHistoryList ->
                    isObservingReadingHistoryList = false
                    _readingHistoryList.value = readingHistoryList.toPersistentList()
                    hasNextReadingHistoryListPage =
                      readingHistoryList.size >=
                          READING_HISTORY_LIST_PER_PAGE_SIZE

                    if (hasNextReadingHistoryListPage) observeHistoryNextPage()
                    else return@onSuccess
                  }
                  .onFailure { throwable ->
                    isObservingReadingHistoryList = false

                    if (throwable is BusinessException.Resource.AccessDenied &&
                      _userId.value == null
                    )
                      return@onFailure

                    _readingHistoryList.value = persistentListOf()
                    hasNextReadingHistoryListPage = false
                    Log.d(
                      TAG,
                      "observeHistoryFirstPage have error: ${throwable.stackTraceToString()}"
                    )
                  }
              }
          } catch (c: CancellationException) {
            throw c
          } catch (e: Exception) {
            isObservingReadingHistoryList = false
            _readingHistoryList.value = persistentListOf()
            hasNextReadingHistoryListPage = false
            Log.d(TAG, "observeHistoryFirstPage have error: ${e.stackTraceToString()}")
          }
        }
      }
  }

  private fun observeHistoryNextPage() {
    if (!hasNextReadingHistoryListPage || isObservingReadingHistoryList) return

    observeHistoryJob =
      viewModelScope.launch {
        isObservingReadingHistoryList = true

        _userId.collectLatest { userId ->
          if (userId == null) {
            cancelObserveHistoryJob()
            return@collectLatest
          }

          val lastReadingHistoryId = _readingHistoryList.value.lastOrNull()?.id

          try {
            observeHistoryUseCase(
              userId = userId,
              limit = READING_HISTORY_LIST_PER_PAGE_SIZE,
              mangaId = mangaIdFromArg,
              lastReadingHistoryId = lastReadingHistoryId
            )
              .collect { result ->
                result
                  .onSuccess { readingHistoryList ->
                    isObservingReadingHistoryList = false
                    _readingHistoryList.value =
                      (_readingHistoryList.value + readingHistoryList).toPersistentList()
                    hasNextReadingHistoryListPage =
                      readingHistoryList.size >=
                          READING_HISTORY_LIST_PER_PAGE_SIZE

                    if (hasNextReadingHistoryListPage) observeHistoryNextPage()
                    else return@onSuccess
                  }
                  .onFailure { throwable ->
                    isObservingReadingHistoryList = false

                    if (throwable is BusinessException.Resource.AccessDenied &&
                      _userId.value == null
                    )
                      return@onFailure

                    hasNextReadingHistoryListPage = false
                    Log.d(
                      TAG,
                      "observeHistoryNextPage have error: ${throwable.stackTraceToString()}"
                    )
                  }
              }
          } catch (c: CancellationException) {
            throw c
          } catch (e: Exception) {
            isObservingReadingHistoryList = false
            hasNextReadingHistoryListPage = false
            Log.d(TAG, "observeHistoryNextPage have error: ${e.stackTraceToString()}")
          }
        }
      }
  }

  fun updateUserId(id: String?) {
    if (_userId.value == id) return
    _userId.value = id
  }

  fun updateChapterLanguage(language: MangaLanguageValue) {
    if (_chapterLanguage.value == language) return
    _chapterLanguage.value = language
    fetchFirstChapter()
    fetchChapterListFirstPage()
  }

  fun retry() {
    if (_mangaDetailsUiState.value is MangaDetailsUiState.Error) fetchMangaDetails()
    if (_startedChapterId.value == null) fetchFirstChapter()
    if (_mangaChaptersUiState.value is BasePaginationUiState.FirstPageError)
      fetchChapterListFirstPage()
    observeIsFavorite()
    observeHistoryFirstPage()
  }

  fun retryFetchChapterListFirstPage() {
    if (_mangaChaptersUiState.value is BasePaginationUiState.FirstPageError)
      fetchChapterListFirstPage()
  }

  fun retryFetchChapterListNextPage() {
    val currentMangaChaptersUiState = _mangaChaptersUiState.value
    if (currentMangaChaptersUiState is BasePaginationUiState.Content<ChapterModel> &&
      currentMangaChaptersUiState.nextPageState == BaseNextPageState.ERROR
    )
      fetchChapterListNextPageInternal(currentMangaChaptersUiState)
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
    private const val STOP_TIMEOUT_MILLIS = 5000L
  }
}
