package com.decoutkhanqindev.dexreader.presentation.ui.manga_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.model.FavoriteManga
import com.decoutkhanqindev.dexreader.domain.usecase.chapter.GetChapterListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.favorite.AddToFavoritesUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.favorite.ObserveIsFavoriteUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.favorite.RemoveFromFavoritesUseCase
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

  private val _userId = MutableStateFlow<String?>(null)
  private val userId = _userId.asStateFlow()

  private val _isFavorite = MutableStateFlow(false)
  val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

  init {
    observeIsFavorite()
    fetchMangaDetails()
    fetchFirstChapter()
    fetchChapterListFirstPage()
  }

  private fun fetchMangaDetails() {
    viewModelScope.launch {
      val mangaDetailsResult = getMangaDetailsUseCase(mangaId = mangaIdFromArg)
      mangaDetailsResult
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
            nextPageState =
              if (!hasNextPage) MangaChaptersNextPageState.NO_MORE_ITEMS
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

  private fun observeIsFavorite() {
    viewModelScope.launch {
      mangaDetailsUiState.collect { currentUiState ->
        if (currentUiState !is MangaDetailsUiState.Success) return@collect
        val mangaId = currentUiState.manga.id

        userId.collectLatest {
          it?.let { userId ->
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
  }

  fun addToFavorites() {
    val currentUiState = _mangaDetailsUiState.value
    if (currentUiState !is MangaDetailsUiState.Success) return

    viewModelScope.launch {
      val manga = currentUiState.manga
      userId.value?.let { userId ->
        val newFavoriteManga = FavoriteManga(
          id = manga.id,
          title = manga.title,
          coverUrl = manga.coverUrl,
          author = manga.author,
          status = manga.status,
          addedAt = null
        )

        val addToFavoriteResult = addToFavoritesUseCase(
          userId = userId,
          manga = newFavoriteManga
        )
        addToFavoriteResult
          .onSuccess { Log.d(TAG, "addToFavorites success") }
          .onFailure {
            Log.d(TAG, "addToFavorites have error: ${it.stackTraceToString()}")
          }
      }
    }
  }

  fun removeFromFavorites() {
    val currentUiState = _mangaDetailsUiState.value
    if (currentUiState !is MangaDetailsUiState.Success) return

    viewModelScope.launch {
      val mangaId = currentUiState.manga.id
      userId.value?.let { userId ->
        val removeFromFavoriteResult = removeFromFavoritesUseCase(
          userId = userId,
          mangaId = mangaId
        )
        removeFromFavoriteResult
          .onSuccess { Log.d(TAG, "removeFromFavorites success") }
          .onFailure {
            Log.d(TAG, "removeFromFavorites have error: ${it.stackTraceToString()}")
          }
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
    if (_mangaDetailsUiState.value is MangaDetailsUiState.Error)
      fetchMangaDetails()
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
    ) fetchChapterListNextPage()
  }

  companion object {
    private const val TAG = "MangaDetailsViewModel"
    private const val FIRST_PAGE = 1
    private const val ASC_ORDER = "asc"
    private const val CHAPTER_LIST_PER_PAGE_SIZE = 20
  }
}