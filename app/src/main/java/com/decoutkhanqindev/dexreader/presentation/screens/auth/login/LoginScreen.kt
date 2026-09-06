package com.decoutkhanqindev.dexreader.presentation.screens.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.decoutkhanqindev.dexreader.presentation.navigation.NavRoute
import com.decoutkhanqindev.dexreader.presentation.screens.auth.login.components.LoginContent
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateClearStack
import com.decoutkhanqindev.dexreader.util.NavTransitions.navigateTo

@Composable
fun LoginScreen(
  navController: NavHostController,
  viewModel: LoginViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LoginContent(
    uiState = uiState,
    modifier = modifier,
    onEmailChange = { viewModel.updateEmail(it) },
    onPasswordChange = { viewModel.updatePassword(it) },
    onSubmitClick = { viewModel.submit() },
    onLoginSuccess = { navController.navigateClearStack<NavRoute.Login>(NavRoute.Main) },
    onRegisterClick = { navController.navigateTo(NavRoute.Register) },
    onForgotPasswordClick = { navController.navigateTo(NavRoute.ForgotPassword) },
    onRetry = { viewModel.retry() },
  )
}