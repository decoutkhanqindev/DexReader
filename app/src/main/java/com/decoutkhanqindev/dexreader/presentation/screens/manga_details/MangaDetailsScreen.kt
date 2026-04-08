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
  viewModel: MangaDetailsViewModel = hiltViewModel(),
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  modifier: Modifier = Modifier,
  onNavigateBack: () -> Unit,
  onNavigateToSearchScreen: () -> Unit,
  onNavigateToLoginScreen: () -> Unit,
  onNavigateCategoryDetailsScreen: (
    categoryId: String,
    categoryTitle: String,
  ) -> Unit,
  onNavigateToReaderScreen: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
) {
  val mangaDetailsUiState by viewModel.mangaDetailsUiState.collectAsStateWithLifecycle()
  val mangaChaptersUiState by viewModel.mangaChaptersUiState.collectAsStateWithLifecycle()
  val chapterLanguage by viewModel.chapterLanguage.collectAsStateWithLifecycle()
  val availableLanguageList by viewModel.availableLanguageList.collectAsStateWithLifecycle()
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
      title = stringResource(R.string.you_must_sign_in_to_favorite_this_manga),
      confirm = stringResource(R.string.sign_in),
      onConfirmClick = {
        isShowFavoritesDialog = false
        onNavigateToLoginScreen()
      },
      onDismissClick = { isShowFavoritesDialog = false },
    )
  }

  BaseDetailsScreen(
    title = stringResource(R.string.manga_details),
    modifier = modifier,
    onNavigateBack = onNavigateBack,
    onNavigateToSearchScreen = onNavigateToSearchScreen
  ) {
    MangaDetailsContent(
        mangaDetailsUiState = mangaDetailsUiState,
        mangaChaptersUiState = mangaChaptersUiState,
        isFavorite = isFavorite,
        chapterLanguage = chapterLanguage,
        availableLanguageList = availableLanguageList,
        readingHistoryList = readingHistoryList,
        startedChapterId = startedChapterId,
        continueChapter = continueChapter,
        modifier = Modifier.fillMaxSize(),
        onReadingClick = onNavigateToReaderScreen,
        onFavoriteClick = {
          if (isUserLoggedIn) {
            if (isFavorite) viewModel.removeFromFavorites()
            else viewModel.addToFavorites()
          } else isShowFavoritesDialog = true
        },
        onLanguageItemClick = { viewModel.updateChapterLanguage(it) },
        onCategoryItemClick = onNavigateCategoryDetailsScreen,
        onChapterItemClick = onNavigateToReaderScreen,
        onFetchChapterListNextPage = viewModel::fetchChapterListNextPage,
        onRetryFetchChapterListNextPage = viewModel::retryFetchChapterListNextPage,
        onRetryFetchChapterListFirstPage = viewModel::retryFetchChapterListFirstPage,
        onRetry = viewModel::retry,
      )
  }
}
