package com.decoutkhanqindev.dexreader.presentation.screens.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.manga.FavoriteMangaModel
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingChartPointModel
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.model.value.settings.ThemeModeValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BaseNextPageState
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.state.BasePaginationUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.settings.SettingsUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.statistics.StatisticsUiState
import com.decoutkhanqindev.dexreader.presentation.screens.profile.ProfileUiState
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions.LogoutButton
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.sections.ProfileEditSection
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.sections.ProfileFavoritesSection
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.sections.ProfileHistorySection
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.sections.ProfileSettingsSection
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.sections.ProfileStatisticsSection
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
  profileUiState: ProfileUiState,
  favoritesUiState: BasePaginationUiState<FavoriteMangaModel>,
  historyUiState: BasePaginationUiState<ReadingHistoryModel>,
  statisticsUiState: StatisticsUiState,
  settingsUiState: SettingsUiState,
  isShowUpdateButton: Boolean,
  modifier: Modifier = Modifier,
  onUpdateNameChange: (String) -> Unit,
  onUpdatePicUrlChange: (String) -> Unit,
  onUpdateClick: () -> Unit,
  onLogoutClick: () -> Unit,
  onLogoutSuccess: () -> Unit,
  onRetryUpdate: () -> Unit,
  onRetryLogout: () -> Unit,
  onFavoriteMangaClick: (String) -> Unit,
  onFavoritesMoreClick: () -> Unit,
  onRetryFavorites: () -> Unit,
  onContinueReadingClick: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
  onHistoryMangaDetailsClick: (String) -> Unit,
  onHistoryMoreClick: () -> Unit,
  onRetryHistory: () -> Unit,
  onStatisticsMoreClick: () -> Unit,
  onRetryStatistics: () -> Unit,
  onThemeOptionClick: (ThemeModeValue) -> Unit,
  onRetryTheme: () -> Unit,
  onRefresh: () -> Unit,
) {
  var isShowUpdateUserSuccessDialog by remember { mutableStateOf(false) }
  var isShowUpdateUserErrorDialog by remember { mutableStateOf(false) }
  var isShowLogoutUserSuccessDialog by remember { mutableStateOf(false) }
  var isShowLogoutUserErrorDialog by remember { mutableStateOf(false) }
  val pullToRefreshState = rememberPullToRefreshState()

  SideEffect(profileUiState.isUpdateUserSuccess) {
    if (profileUiState.isUpdateUserSuccess) isShowUpdateUserSuccessDialog = true
  }

  SideEffect(profileUiState.isUpdateUserError) {
    if (profileUiState.isUpdateUserError) isShowUpdateUserErrorDialog = true
  }

  SideEffect(profileUiState.isLogoutUserSuccess) {
    if (profileUiState.isLogoutUserSuccess) isShowLogoutUserSuccessDialog = true
  }

  SideEffect(profileUiState.isLogoutUserError) {
    if (profileUiState.isLogoutUserError) isShowLogoutUserErrorDialog = true
  }

  PullToRefreshBox(
    state = pullToRefreshState,
    isRefreshing = false,
    onRefresh = onRefresh,
    modifier = if (profileUiState.isLoading) {
      modifier.blurBackground(alphas = persistentListOf(0.7f, 0.7f))
    } else modifier,
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      ProfileEditSection(
        uiState = profileUiState,
        isShowUpdateButton = isShowUpdateButton,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        onUpdateNameChange = onUpdateNameChange,
        onUpdatePicUrlChange = onUpdatePicUrlChange,
        onUpdateClick = onUpdateClick,
      )

      HorizontalDivider(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp))

      ProfileFavoritesSection(
        uiState = favoritesUiState,
        modifier = Modifier.fillMaxWidth(),
        onMangaClick = onFavoriteMangaClick,
        onMoreClick = onFavoritesMoreClick,
        onRetry = onRetryFavorites,
      )

      ProfileHistorySection(
        uiState = historyUiState,
        modifier = Modifier.fillMaxWidth(),
        onContinueReadingClick = onContinueReadingClick,
        onMangaDetailsClick = onHistoryMangaDetailsClick,
        onMoreClick = onHistoryMoreClick,
        onRetry = onRetryHistory,
      )

      ProfileStatisticsSection(
        uiState = statisticsUiState,
        modifier = Modifier.fillMaxWidth(),
        onMoreClick = onStatisticsMoreClick,
        onRetry = onRetryStatistics,
      )

      ProfileSettingsSection(
        uiState = settingsUiState,
        modifier = Modifier.fillMaxWidth(),
        onThemeOptionClick = onThemeOptionClick,
        onRetry = onRetryTheme,
      )

      Spacer(modifier = Modifier.weight(1f))

      LogoutButton(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .padding(bottom = 78.dp),
        onLogoutClick = onLogoutClick,
      )
    }
  }

  when {
    profileUiState.isLoading -> LoadingScreen(modifier = Modifier.fillMaxSize())

    profileUiState.isUpdateUserError -> {
      if (isShowUpdateUserErrorDialog) {
        AlertDialog(
          title = stringResource(R.string.update_profile_failed),
          onConfirmClick = {
            isShowUpdateUserErrorDialog = false
            onRetryUpdate()
          },
          onDismissClick = { isShowUpdateUserErrorDialog = false },
        )
      }
    }

    profileUiState.isLogoutUserError -> {
      if (isShowLogoutUserErrorDialog) {
        AlertDialog(
          title = stringResource(R.string.logout_failed_please_try_again),
          onConfirmClick = {
            isShowLogoutUserErrorDialog = false
            onRetryLogout()
          },
          onDismissClick = { isShowLogoutUserErrorDialog = false },
        )
      }
    }

    profileUiState.isUpdateUserSuccess -> {
      if (isShowUpdateUserSuccessDialog) {
        AlertDialog(
          icon = Icons.Default.Done,
          title = stringResource(R.string.your_profile_has_been_updated_successfully),
          confirm = stringResource(R.string.ok),
          isEnableDismiss = false,
          onConfirmClick = { isShowUpdateUserSuccessDialog = false },
        )
      }
    }

    profileUiState.isLogoutUserSuccess -> {
      if (isShowLogoutUserSuccessDialog) {
        AlertDialog(
          icon = Icons.Default.Done,
          title = stringResource(R.string.logout_successful),
          confirm = stringResource(R.string.ok),
          isEnableDismiss = false,
          onConfirmClick = {
            isShowLogoutUserSuccessDialog = false
            onLogoutSuccess()
          },
        )
      }
    }
  }
}

private val previewUser = UserModel(
  id = "u-001",
  name = "Nguyen Van A",
  email = "nguyenvana@email.com",
  avatarUrl = null,
)

private val previewFavoriteList = persistentListOf(
  FavoriteMangaModel(
    id = "1",
    title = "One Piece",
    coverUrl = "",
    author = "Eiichiro Oda",
    status = MangaStatusValue.ON_GOING,
    rating = "9.1",
    follows = "2.3M",
  ),
  FavoriteMangaModel(
    id = "2",
    title = "Fullmetal Alchemist",
    coverUrl = "",
    author = "Hiromu Arakawa",
    status = MangaStatusValue.COMPLETED,
    rating = "9.2",
    follows = "1.1M",
  ),
)

private val previewHistoryList = persistentListOf(
  ReadingHistoryModel(
    id = "rh-001",
    mangaId = "m-001",
    mangaTitle = "One Piece",
    mangaCoverUrl = "",
    chapterId = "c-001",
    chapterTitle = "Romance Dawn",
    chapterNumber = "1",
    chapterVolume = "1",
    lastReadPage = 12,
    pageCount = 46,
    lastReadAt = "2 hours ago",
  ),
)

private val previewStatisticsUiState = StatisticsUiState.Success(
  monthlyBreakdown = persistentListOf(
    ReadingChartPointModel(id = "2026-06", label = "Jun", minutes = 320),
    ReadingChartPointModel(id = "2026-07", label = "Jul", minutes = 540),
    ReadingChartPointModel(id = "2026-08", label = "Aug", minutes = 210),
  ),
)

@Composable
private fun ProfileContentPreviewHost(
  uiState: ProfileUiState,
  isShowUpdateButton: Boolean = false,
) {
  ProfileContent(
    profileUiState = uiState,
    favoritesUiState = BasePaginationUiState.Content(
      currentList = previewFavoriteList,
      nextPageState = BaseNextPageState.IDLE
    ),
    historyUiState = BasePaginationUiState.Content(
      currentList = previewHistoryList,
      nextPageState = BaseNextPageState.IDLE
    ),
    statisticsUiState = previewStatisticsUiState,
    settingsUiState = SettingsUiState(
      appliedThemeOption = ThemeModeValue.SYSTEM,
      selectedThemeOption = ThemeModeValue.SYSTEM,
    ),
    isShowUpdateButton = isShowUpdateButton,
    modifier = Modifier.fillMaxSize(),
    onUpdateNameChange = {},
    onUpdatePicUrlChange = {},
    onUpdateClick = {},
    onLogoutClick = {},
    onLogoutSuccess = {},
    onRetryUpdate = {},
    onRetryLogout = {},
    onFavoriteMangaClick = {},
    onFavoritesMoreClick = {},
    onRetryFavorites = {},
    onContinueReadingClick = { _, _, _ -> },
    onHistoryMangaDetailsClick = {},
    onHistoryMoreClick = {},
    onRetryHistory = {},
    onStatisticsMoreClick = {},
    onRetryStatistics = {},
    onThemeOptionClick = {},
    onRetryTheme = {},
    onRefresh = {},
  )
}

@Preview
@Composable
private fun ProfileContentIdlePreview() {
  DexReaderTheme {
    ProfileContentPreviewHost(uiState = ProfileUiState(currentUser = previewUser))
  }
}

@Preview
@Composable
private fun ProfileContentWithUpdateButtonPreview() {
  DexReaderTheme {
    ProfileContentPreviewHost(
      uiState = ProfileUiState(currentUser = previewUser, newName = "New Name"),
      isShowUpdateButton = true,
    )
  }
}

@Preview
@Composable
private fun ProfileContentLoadingPreview() {
  DexReaderTheme {
    ProfileContentPreviewHost(
      uiState = ProfileUiState(currentUser = previewUser, isLoading = true)
    )
  }
}

@Preview
@Composable
private fun ProfileContentUpdateErrorPreview() {
  DexReaderTheme {
    ProfileContentPreviewHost(
      uiState = ProfileUiState(currentUser = previewUser, isUpdateUserError = true)
    )
  }
}
