package com.decoutkhanqindev.dexreader.presentation.screens.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.search.components.SearchContent
import com.decoutkhanqindev.dexreader.presentation.screens.search.components.actions.SearchBar
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateBack
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateTo


@Composable
fun SearchScreen(
  navController: NavHostController,
  viewModel: SearchViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val suggestionsUiState by viewModel.suggestionsUiState.collectAsStateWithLifecycle()
  val resultsUiState by viewModel.resultsUiState.collectAsStateWithLifecycle()
  val suggestionList by viewModel.suggestionList.collectAsStateWithLifecycle()
  val query by viewModel.query.collectAsStateWithLifecycle()
  var isExpanded by remember { mutableStateOf(false) }

  BackHandler { navController.navigateBack() }

  BaseDetailsScreen(
    modifier = modifier,
    topBar = {
      SearchBar(
        query = query,
        modifier = Modifier.fillMaxWidth(),
        onQueryChange = {
          viewModel.updateQuery(it)
          isExpanded = true
        },
        onSearch = {
          viewModel.fetchMangaListFirstPage()
          isExpanded = false
        },
        onNavigateBack = { navController.navigateBack() },
      )
    },
  ) {
    SearchContent(
      query = query,
      suggestionsUiState = suggestionsUiState,
      resultsUiState = resultsUiState,
      isExpanded = isExpanded,
      suggestionList = suggestionList,
      modifier = Modifier.fillMaxSize(),
      onSelectedSuggestion = {
        viewModel.updateQuery(it)
        viewModel.fetchMangaListFirstPage()
        isExpanded = false
      },
      onSelectedManga = { mangaId -> navController.navigateTo(NavRoute.MangaDetails(mangaId)) },
      onFetchMangaListNextPage = { viewModel.fetchMangaListNextPage() },
      onRetryFetchMangaListNextPage = { viewModel.retryFetchMangaListNextPage() },
      onRetry = { viewModel.retry() },
      onRefresh = { viewModel.fetchMangaListFirstPage() },
    )
  }
}



