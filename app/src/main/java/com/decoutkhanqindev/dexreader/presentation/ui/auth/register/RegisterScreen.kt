package com.decoutkhanqindev.dexreader.presentation.ui.auth.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.ui.auth.register.components.RegisterContent

@Composable
fun RegisterScreen(
  onNavigateBack: () -> Unit,
  onRegisterSuccess: () -> Unit,
  viewModel: RegisterViewModel = hiltViewModel(),
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  RegisterContent(
    uiState = uiState,
    onEmailChange = { viewModel.updateEmailField(email = it) },
    onPasswordChange = { viewModel.updatePasswordField(password = it) },
    onConfirmPasswordChange = { viewModel.updateConfirmPasswordField(confirmPassword = it) },
    onNameChange = { viewModel.updateNameField(name = it) },
    onSubmitClick = { viewModel.registerUser() },
    onRegisterSuccess = onRegisterSuccess,
    onNavigateBack = onNavigateBack,
    modifier = modifier
  )
}