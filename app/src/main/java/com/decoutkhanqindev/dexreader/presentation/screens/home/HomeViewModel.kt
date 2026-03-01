package com.decoutkhanqindev.dexreader.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetLatestUpdateMangaListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetNewReleaseMangaListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetTopRatedMangaListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetTrendingMangaListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getLatestUploadedMangaListUseCase: GetLatestUpdateMangaListUseCase,
  private val getTrendingMangaListUseCase: GetTrendingMangaListUseCase,
  private val getNewReleaseMangaListUseCase: GetNewReleaseMangaListUseCase,
  private val getCompletedMangaListUseCase: GetTopRatedMangaListUseCase,
) : ViewModel() {
  private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
  val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

  init {
    fetchMangaLists()
  }

  private fun fetchMangaLists() {
    viewModelScope.launch {
      _uiState.value = HomeUiState.Loading

      val latestUpdatesMangaListDef = async { getLatestUploadedMangaListUseCase() }
      val trendingMangaListDef = async { getTrendingMangaListUseCase() }
      val newReleaseMangaListDef = async { getNewReleaseMangaListUseCase() }
      val topRatedMangaListDef = async { getCompletedMangaListUseCase() }

      val results = awaitAll(
        latestUpdatesMangaListDef,
        trendingMangaListDef,
        newReleaseMangaListDef,
        topRatedMangaListDef
      )

      if (results.all { it.isSuccess }) {
        val latestUpdatesMangaList = results[0].getOrThrow()
        val trendingMangaList = results[1].getOrThrow()
        val newReleaseMangaList = results[2].getOrThrow()
        val topRatedMangaList = results[3].getOrThrow()

        _uiState.value = HomeUiState.Success(
          latestUpdatesMangaList = latestUpdatesMangaList,
          trendingMangaList = trendingMangaList,
          newReleaseMangaList = newReleaseMangaList,
          topRatedMangaList = topRatedMangaList
        )
      } else {
        val error = results.firstOrNull { it.isFailure }?.exceptionOrNull()
        _uiState.value = HomeUiState.Error
        Log.e(TAG, "fetchMangaLists have error: ${error?.stackTraceToString()}")
      }
    }
  }

  fun retry() {
    if (_uiState.value is HomeUiState.Error) fetchMangaLists()
  }

  companion object {
    private const val TAG = "HomeViewModel"
  }
}

