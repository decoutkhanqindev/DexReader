package com.decoutkhanqindev.dexreader.presentation.screens.manga_details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.MangaDetailsContent

@Composable
fun MangaDetailsScreen(
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  onNavigateBack: () -> Unit,
  onNavigateToSearchScreen: () -> Unit,
  onNavigateToLoginScreen: () -> Unit,
  onReadingClick: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  onNavigateCategoryScreen: (
    categoryId: String,
    categoryTitle: String,
  ) -> Unit,
  onNavigateToReaderScreen: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  viewModel: MangaDetailsViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val mangaDetailsUiState by viewModel.mangaDetailsUiState.collectAsStateWithLifecycle()
  val mangaChaptersUiState by viewModel.mangaChaptersUiState.collectAsStateWithLifecycle()
  val chapterLanguage by viewModel.chapterLanguage.collectAsStateWithLifecycle()
  val availableLanguages by viewModel.availableLanguages.collectAsStateWithLifecycle()
  val readingHistoryList by viewModel.readingHistoryList.collectAsStateWithLifecycle()
  val startedChapterId by viewModel.startedChapterId.collectAsStateWithLifecycle()
  val continueChapter by viewModel.continueChapter.collectAsStateWithLifecycle()
  val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()
  var isShowFavoritesDialog by rememberSaveable { mutableStateOf(false) }

  LaunchedEffect(isUserLoggedIn, currentUser?.id) {
    if (isUserLoggedIn && currentUser != null) viewModel.updateUserId(id = currentUser.id)
    else viewModel.updateUserId(id = null)
  }

  if (isShowFavoritesDialog) {
    NotificationDialog(
      onConfirmClick = {
        isShowFavoritesDialog = false
        onNavigateToLoginScreen()
      },
      title = stringResource(R.string.you_must_sign_in_to_favorite_this_manga),
      onDismissClick = { isShowFavoritesDialog = false },
      confirm = stringResource(R.string.sign_in),
    )
  }

  BaseDetailsScreen(
    title = stringResource(R.string.manga_details),
    onNavigateBack = onNavigateBack,
    onNavigateToSearchScreen = onNavigateToSearchScreen,
    content = {
      MangaDetailsContent(
        mangaDetailsUiState = mangaDetailsUiState,
        mangaChaptersUiState = mangaChaptersUiState,
        onReadingClick = onReadingClick,
        isFavorite = isFavorite,
        onFavoriteClick = {
          if (isUserLoggedIn) {
            if (isFavorite) viewModel.removeFromFavorites()
            else viewModel.addToFavorites()
          } else isShowFavoritesDialog = true
        },
        chapterLanguage = chapterLanguage,
        availableLanguages = availableLanguages,
        onSelectedLanguage = { viewModel.updateChapterLanguage(it) },
        onSelectedCategory = onNavigateCategoryScreen,
        onSelectedChapter = onNavigateToReaderScreen,
        onFetchChapterListNextPage = viewModel::fetchChapterListNextPage,
        onRetryFetchChapterListNextPage = viewModel::retryFetchChapterListNextPage,
        onRetryFetchChapterListFirstPage = viewModel::retryFetchChapterListFirstPage,
        onRetry = viewModel::retry,
        modifier = Modifier.fillMaxSize(),
        readingHistoryList = readingHistoryList,
        startedChapterId = startedChapterId,
        continueChapter = continueChapter,
      )
    },
    modifier = modifier
  )
}
