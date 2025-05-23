package com.decoutkhanqindev.dexreader.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetCompletedMangaListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetLatestUploadedMangaListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetNewReleaseMangaListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetTrendingMangaListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getLatestUploadedMangaListUseCase: GetLatestUploadedMangaListUseCase,
  private val getTrendingMangaListUseCase: GetTrendingMangaListUseCase,
  private val getNewReleaseMangaListUseCase: GetNewReleaseMangaListUseCase,
  private val getCompletedMangaListUseCase: GetCompletedMangaListUseCase,
) : ViewModel() {
  private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
  val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

  init {
    loadData()
  }

  fun loadData() {
    viewModelScope.launch {
      _uiState.value = HomeUiState.Loading

      val latestUploadedMangaListDef = async { getLatestUploadedMangaListUseCase() }
      val trendingMangaListDef = async { getTrendingMangaListUseCase() }
      val newReleaseMangaListDef = async { getNewReleaseMangaListUseCase() }
      val completedMangaListDef = async { getCompletedMangaListUseCase() }

      val results = awaitAll(
        latestUploadedMangaListDef,
        trendingMangaListDef,
        newReleaseMangaListDef,
        completedMangaListDef
      )

      if (results.all { it.isSuccess }) {
        val latestUploadedMangaList = results[0].getOrThrow()
        val trendingMangaList = results[1].getOrThrow()
        val newReleaseMangaList = results[2].getOrThrow()
        val completedMangaList = results[3].getOrThrow()

        _uiState.value = HomeUiState.Success(
          latestUploadedMangaList,
          trendingMangaList,
          newReleaseMangaList,
          completedMangaList
        )
      } else {
        val error = results.firstOrNull { it.isFailure }?.exceptionOrNull()
        _uiState.value = HomeUiState.Error
        Log.e("HomeViewModel", "loadData have error: ${error?.message}")
      }
    }
  }
}

