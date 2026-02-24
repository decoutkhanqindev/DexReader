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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseDetailsScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.MangaDetailsContent
import com.decoutkhanqindev.dexreader.util.LanguageCodec.toFullLanguageName
import com.decoutkhanqindev.dexreader.util.LanguageCodec.toLanguageCode

@Composable
fun MangaDetailsScreen(
  isUserLoggedIn: Boolean,
  currentUser: User?,
  onNavigateBack: () -> Unit,
  onSearchClick: () -> Unit,
  onSignInClick: () -> Unit,
  onReadingClick: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  onSelectedCategory: (
    categoryId: String,
    categoryTitle: String,
  ) -> Unit,
  onSelectedChapter: (
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
  val readingHistoryList by viewModel.readingHistoryList.collectAsStateWithLifecycle()
  val startedChapter by viewModel.startedChapter.collectAsStateWithLifecycle()
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
      onDismissClick = { isShowFavoritesDialog = false },
      confirm = stringResource(R.string.sign_in),
      onConfirmClick = {
        isShowFavoritesDialog = false
        onSignInClick()
      },
    )
  }

  BaseDetailsScreen(
    title = stringResource(R.string.manga_details),
    onNavigateBack = onNavigateBack,
    onSearchClick = onSearchClick,
    content = {
      MangaDetailsContent(
        mangaDetailsUiState = mangaDetailsUiState,
        mangaChaptersUiState = mangaChaptersUiState,
        readingHistoryList = readingHistoryList,
        startedChapter = startedChapter,
        continueChapter = continueChapter,
        onReadingClick = onReadingClick,
        isFavorite = isFavorite,
        onFavoriteClick = {
          if (isUserLoggedIn) {
            if (isFavorite) viewModel.removeFromFavorites()
            else viewModel.addToFavorites()
          } else {
            isShowFavoritesDialog = true
          }
        },
        chapterLanguage = chapterLanguage.toFullLanguageName(),
        onSelectedLanguage = { viewModel.updateChapterLanguage(it.toLanguageCode()) },
        onSelectedCategory = onSelectedCategory,
        onSelectedChapter = onSelectedChapter,
        onFetchChapterListNextPage = viewModel::fetchChapterListNextPage,
        onRetryFetchChapterListNextPage = viewModel::retryFetchChapterListNextPage,
        onRetryFetchChapterListFirstPage = viewModel::retryFetchChapterListFirstPage,
        onRetry = viewModel::retry,
        modifier = Modifier.fillMaxSize()
      )
    },
    modifier = modifier
  )
}
