package com.decoutkhanqindev.dexreader.presentation.screens.auth.register

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.auth.register.components.RegisterContent
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateBack
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateClearStack

@Composable
fun RegisterScreen(
  navController: NavHostController,
  viewModel: RegisterViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  BackHandler { navController.navigateBack() }

  RegisterContent(
    uiState = uiState,
    modifier = modifier,
    onEmailChange = { viewModel.updateEmail(it) },
    onPasswordChange = { viewModel.updatePassword(it) },
    onConfirmPasswordChange = { viewModel.updateConfirmPassword(it) },
    onNameChange = { viewModel.updateName(it) },
    onSubmitClick = { viewModel.submit() },
    onRegisterSuccess = { navController.navigateClearStack<NavRoute.Register>(NavRoute.Login) },
    onNavigateBack = { navController.navigateBack() },
    onRetry = { viewModel.retry() },
  )
}