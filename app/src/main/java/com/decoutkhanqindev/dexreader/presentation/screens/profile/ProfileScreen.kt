package com.decoutkhanqindev.dexreader.presentation.screens.profile


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.model.value.bottom_bar.BottomTabItemValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseScreen
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmerHighlight
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.IdleScreen
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions.SignInButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.favorites.FavoritesViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.history.HistoryViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.settings.SettingsViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.viewmodels.statistics.StatisticsViewModel
import com.decoutkhanqindev.dexreader.presentation.screens.profile.components.ProfileContent

@Composable
fun ProfileScreen(
  settingsViewModel: SettingsViewModel,
  favoritesViewModel: FavoritesViewModel = hiltViewModel(),
  historyViewModel: HistoryViewModel = hiltViewModel(),
  statisticsViewModel: StatisticsViewModel = hiltViewModel(),
  profileViewModel: ProfileViewModel = hiltViewModel(),
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  modifier: Modifier = Modifier,
  onNavigateToLoginScreen: () -> Unit,
  onNavigateToFavoritesScreen: () -> Unit,
  onNavigateToHistoryScreen: () -> Unit,
  onNavigateToStatisticsScreen: () -> Unit,
  onNavigateToMangaDetailScreen: (String) -> Unit,
  onNavigateToReaderScreen: (
    chapterId: String,
    lastReadPage: Int,
    mangaId: String,
  ) -> Unit,
) {
  val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()
  val favoritesUiState by favoritesViewModel.uiState.collectAsStateWithLifecycle()
  val historyUiState by historyViewModel.historyUiState.collectAsStateWithLifecycle()
  val statisticsUiState by statisticsViewModel.uiState.collectAsStateWithLifecycle()
  val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
  val isShowUpdateButton by remember {
    derivedStateOf {
      val nameChanged = uiState.newName != null
          && uiState.newName != uiState.currentUser?.name
      val picChanged = uiState.newAvatarUrl != null
          && uiState.newAvatarUrl != uiState.currentUser?.avatarUrl
      nameChanged || picChanged
    }
  }

  SideEffect(isUserLoggedIn, currentUser) {
    if (isUserLoggedIn && currentUser != null) profileViewModel.updateCurrentUser(value = currentUser)
    else profileViewModel.updateCurrentUser(value = null)
  }

  SideEffect(isUserLoggedIn, currentUser?.id) {
    val userId = if (isUserLoggedIn && currentUser != null) currentUser.id else null
    favoritesViewModel.updateUserId(userId = userId)
    historyViewModel.updateUserId(userId = userId)
    statisticsViewModel.updateUserId(userId = userId)
  }

  BaseScreen(
    selectedTab = BottomTabItemValue.PROFILE,
    isSearchEnabled = false,
    modifier = modifier,
  ) {
    if (isUserLoggedIn) {
      ProfileContent(
        profileUiState = uiState,
        favoritesUiState = favoritesUiState,
        historyUiState = historyUiState,
        statisticsUiState = statisticsUiState,
        settingsUiState = settingsUiState,
        isShowUpdateButton = isShowUpdateButton,
        modifier = Modifier.fillMaxSize(),
        onUpdateNameChange = { profileViewModel.updateUserName(it) },
        onUpdatePicUrlChange = { profileViewModel.updateUserPicUrl(it) },
        onUpdateClick = { profileViewModel.updateUserProfile() },
        onLogoutClick = { profileViewModel.logoutUser() },
        onLogoutSuccess = {},
        onRetryUpdate = { profileViewModel.retryUpdateUserProfile() },
        onRetryLogout = { profileViewModel.retryLogoutUser() },
        onFavoriteMangaClick = onNavigateToMangaDetailScreen,
        onFavoritesMoreClick = onNavigateToFavoritesScreen,
        onRetryFavorites = { favoritesViewModel.retry() },
        onContinueReadingClick = onNavigateToReaderScreen,
        onHistoryMangaDetailsClick = onNavigateToMangaDetailScreen,
        onHistoryMoreClick = onNavigateToHistoryScreen,
        onRetryHistory = { historyViewModel.retryObserveHistoryFirstPage() },
        onStatisticsMoreClick = onNavigateToStatisticsScreen,
        onRetryStatistics = { statisticsViewModel.retry() },
        onThemeOptionClick = {
          settingsViewModel.updateThemeOption(it)
          settingsViewModel.saveThemeOption()
        },
        onRetryTheme = { settingsViewModel.retry() },
        onRefresh = {
          favoritesViewModel.refresh()
          historyViewModel.refresh()
          statisticsViewModel.refresh()
        },
      )
    } else {
      Column(modifier = Modifier.fillMaxSize()) {
        IdleScreen(
          message = stringResource(R.string.please_sign_in_to_view_your_profile),
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
        )

        SignInButton(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 78.dp),
          onSignInClick = onNavigateToLoginScreen,
        )
      }
    }
  }
}
