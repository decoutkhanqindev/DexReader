package com.decoutkhanqindev.dexreader.presentation.ui.manga_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.FavoriteManga
import com.decoutkhanqindev.dexreader.domain.usecase.chapter.GetChapterListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.favorites.AddToFavoritesUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.favorites.ObserveIsFavoriteUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.favorites.RemoveFromFavoritesUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetMangaDetailsUseCase
import com.decoutkhanqindev.dexreader.presentation.navigation.NavDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaDetailsViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val getMangaDetailsUseCase: GetMangaDetailsUseCase,
  private val getChapterListUseCase: GetChapterListUseCase,
  private val addToFavoritesUseCase: AddToFavoritesUseCase,
  private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
  private val observeIsFavoriteUseCase: ObserveIsFavoriteUseCase
) : ViewModel() {
  private val mangaIdFromArg: String =
    checkNotNull(savedStateHandle[NavDestination.MangaDetailsDestination.MANGA_ID_ARG])

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

  private val _isFavorite = MutableStateFlow(false)
  val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

  private val _userId = MutableStateFlow<String?>(null)

  init {
    observeIsFavorite()
    fetchMangaDetails()
    fetchFirstChapter()
    fetchChapterListFirstPage()
  }

  private fun fetchMangaDetails() {
    viewModelScope.launch {
      getMangaDetailsUseCase(mangaId = mangaIdFromArg)
        .onSuccess { _mangaDetailsUiState.value = MangaDetailsUiState.Success(manga = it) }
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
        translatedLanguage = chapterLanguage.value,
        volumeOrder = ASC_ORDER,
        chapterOrder = ASC_ORDER
      )
        .onSuccess {
          if (it.isNotEmpty()) _firstChapterId.value = it.first().id
          else _firstChapterId.value = null
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

      getChapterListUseCase(
        mangaId = mangaIdFromArg,
        translatedLanguage = chapterLanguage.value
      )
        .onSuccess { chapterList ->
          val hasNextPage = chapterList.size >= CHAPTER_LIST_PER_PAGE_SIZE
          _mangaChaptersUiState.value = MangaChaptersUiState.Content(
            chapterList = chapterList,
            currentPage = FIRST_PAGE,
            nextPageState =
              if (!hasNextPage) MangaChaptersNextPageState.NO_MORE_ITEMS
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
    when (val currentMangaChaptersUiState = _mangaChaptersUiState.value) {
      MangaChaptersUiState.FirstPageLoading,
      MangaChaptersUiState.FirstPageError,
        -> return

      is MangaChaptersUiState.Content -> {
        when (currentMangaChaptersUiState.nextPageState) {
          MangaChaptersNextPageState.LOADING,
          MangaChaptersNextPageState.NO_MORE_ITEMS
            -> return

          MangaChaptersNextPageState.ERROR -> retryFetchChapterListNextPage()

          MangaChaptersNextPageState.IDLE -> fetchChapterListNextPageInternal(
            currentMangaChaptersUiState
          )
        }
      }
    }
  }

  private fun fetchChapterListNextPageInternal(currentMangaChaptersUiState: MangaChaptersUiState.Content) {
    viewModelScope.launch {
      _mangaChaptersUiState.value =
        currentMangaChaptersUiState.copy(nextPageState = MangaChaptersNextPageState.LOADING)

      val currentMangaList = currentMangaChaptersUiState.chapterList
      val nextPage: Int = currentMangaChaptersUiState.currentPage + 1

      getChapterListUseCase(
        mangaId = mangaIdFromArg,
        offset = currentMangaList.size,
        translatedLanguage = chapterLanguage.value
      )
        .onSuccess { nextChapterList ->
          val updatedChapterList = currentMangaList + nextChapterList
          val hasNextPage = nextChapterList.size >= CHAPTER_LIST_PER_PAGE_SIZE
          _mangaChaptersUiState.value = currentMangaChaptersUiState.copy(
            chapterList = updatedChapterList,
            currentPage = nextPage,
            nextPageState =
              if (!hasNextPage) MangaChaptersNextPageState.NO_MORE_ITEMS
              else MangaChaptersNextPageState.IDLE
          )
        }
        .onFailure {
          _mangaChaptersUiState.value =
            currentMangaChaptersUiState.copy(nextPageState = MangaChaptersNextPageState.ERROR)
          Log.d(
            TAG,
            "fetchChapterListNextPageInternal have error: ${it.stackTraceToString()}"
          )
        }
    }
  }

  private fun observeIsFavorite() {
    viewModelScope.launch {
      _mangaDetailsUiState.collect { currentUiState ->
        if (currentUiState !is MangaDetailsUiState.Success) return@collect
        val mangaId = currentUiState.manga.id

        _userId.collectLatest { userId ->
          if (userId == null) {
            _isFavorite.value = false
            return@collectLatest
          }

          observeIsFavoriteUseCase(
            userId = userId,
            mangaId = mangaId
          ).collect { result ->
            result
              .onSuccess { _isFavorite.value = it }
              .onFailure {
                _isFavorite.value = false
                Log.d(TAG, "observeIsFavorite have error: ${it.stackTraceToString()}")
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
          .onSuccess { Log.d(TAG, "addToFavorites success") }
          .onFailure { Log.d(TAG, "addToFavorites have error: ${it.stackTraceToString()}") }
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
          .onFailure { Log.d(TAG, "removeFromFavorites have error: ${it.stackTraceToString()}") }
      }
    }
  }

  fun updateUserId(id: String) {
    if (_userId.value == id) return
    _userId.value = id
  }

  fun updateChapterLanguage(language: String) {
    if (_chapterLanguage.value == language) return
    _chapterLanguage.value = language
    fetchFirstChapter()
    fetchChapterListFirstPage()
  }

  fun retry() {
    if (_mangaDetailsUiState.value is MangaDetailsUiState.Error) fetchMangaDetails()
    if (_firstChapterId.value == null) fetchFirstChapter()
    if (_mangaChaptersUiState.value is MangaChaptersUiState.FirstPageError)
      fetchChapterListFirstPage()
  }

  fun retryFetchChapterListFirstPage() {
    if (_mangaChaptersUiState.value is MangaChaptersUiState.FirstPageLoading)
      fetchChapterListFirstPage()
  }

  fun retryFetchChapterListNextPage() {
    val currentMangaChaptersUiState = _mangaChaptersUiState.value
    if (currentMangaChaptersUiState is MangaChaptersUiState.Content &&
      currentMangaChaptersUiState.nextPageState == MangaChaptersNextPageState.ERROR
    ) fetchChapterListNextPageInternal(currentMangaChaptersUiState)
  }

  companion object {
    private const val TAG = "MangaDetailsViewModel"
    private const val FIRST_PAGE = 1
    private const val ASC_ORDER = "asc"
    private const val CHAPTER_LIST_PER_PAGE_SIZE = 20
  }
}