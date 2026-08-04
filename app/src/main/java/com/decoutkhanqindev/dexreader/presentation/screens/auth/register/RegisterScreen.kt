package com.decoutkhanqindev.dexreader.presentation.screens.auth.register

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.screens.auth.register.components.RegisterContent

@Composable
fun RegisterScreen(
  viewModel: RegisterViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
  onNavigateBack: () -> Unit,
  onNavigateToLoginScreen: () -> Unit,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  BackHandler { onNavigateBack() }

  RegisterContent(
    uiState = uiState,
    modifier = modifier,
    onEmailChange = { viewModel.updateEmail(it) },
    onPasswordChange = { viewModel.updatePassword(it) },
    onConfirmPasswordChange = { viewModel.updateConfirmPassword(it) },
    onNameChange = { viewModel.updateName(it) },
    onSubmitClick = { viewModel.submit() },
    onRegisterSuccess = onNavigateToLoginScreen,
    onNavigateBack = onNavigateBack,
    onRetry = { viewModel.retry() },
  )
}