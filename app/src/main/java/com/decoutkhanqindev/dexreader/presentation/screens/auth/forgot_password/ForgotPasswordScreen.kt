package com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password.components.ForgotPasswordContent
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateBack
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateClearStack

@Composable
fun ForgotPasswordScreen(
  navController: NavHostController,
  viewModel: ForgotPasswordViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  BackHandler { navController.navigateBack() }

  ForgotPasswordContent(
    uiState = uiState,
    modifier = modifier,
    onEmailChange = { viewModel.updateEmail(it) },
    onSubmitClick = { viewModel.submit() },
    onSubmitSuccess = { navController.navigateClearStack<NavRoute.ForgotPassword>(NavRoute.Login) },
    onNavigateBack = { navController.navigateBack() },
    onRetry = { viewModel.retry() },
  )
}