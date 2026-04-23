package com.decoutkhanqindev.dexreader.presentation.screens.auth.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.error.UserError
import com.decoutkhanqindev.dexreader.presentation.screens.auth.EmailInputField
import com.decoutkhanqindev.dexreader.presentation.screens.auth.NameInputField
import com.decoutkhanqindev.dexreader.presentation.screens.auth.PasswordInputField
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.SubmitButton
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun RegisterForm(
  email: String,
  emailError: UserError?,
  password: String,
  passwordError: UserError?,
  confirmPassword: String,
  confirmPasswordError: UserError?,
  name: String,
  nameError: UserError?,
  modifier: Modifier = Modifier,
  onEmailChange: (String) -> Unit,
  onPasswordChange: (String) -> Unit,
  onConfirmPasswordChange: (String) -> Unit,
  onNameChange: (String) -> Unit,
  onSubmitClick: () -> Unit,
  onNavigateBack: () -> Unit,
) {
  Card(
    modifier = modifier.verticalScroll(rememberScrollState()),
    shape = RoundedCornerShape(16.dp),
    colors =
      CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
      )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = stringResource(R.string.sign_up),
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp),
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineMedium,
      )

      Spacer(modifier = Modifier.height(16.dp))

      EmailInputField(
        value = email,
        error = emailError,
        modifier = Modifier.fillMaxWidth(),
      ) { onEmailChange(it) }

      PasswordInputField(
        value = password,
        error = passwordError,
        modifier = Modifier.fillMaxWidth(),
      ) { onPasswordChange(it) }

      PasswordInputField(
        value = confirmPassword,
        isConfirmed = true,
        error = confirmPasswordError,
        modifier = Modifier.fillMaxWidth(),
      ) { onConfirmPasswordChange(it) }

      NameInputField(
        value = name,
        error = nameError,
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp),
      ) { onNameChange(it) }

      SubmitButton(
        title = stringResource(R.string.sign_up),
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp),
      ) { onSubmitClick() }

      ActionButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = onNavigateBack
      ) {
        Text(
          text = stringResource(R.string.back),
          color = MaterialTheme.colorScheme.inverseSurface,
          fontWeight = FontWeight.ExtraBold,
          style = MaterialTheme.typography.titleMedium,
        )
      }
    }
  }
}

@Preview
@Composable
private fun RegisterFormPreview() {
  DexReaderTheme {
    RegisterForm(
      email = "nguyenvana@gmail.com",
      emailError = null,
      password = "myPassword123",
      passwordError = null,
      confirmPassword = "myPassword123",
      confirmPasswordError = null,
      name = "Nguyen Van A",
      nameError = null,
      modifier = Modifier.fillMaxSize(),
      onEmailChange = {},
      onPasswordChange = {},
      onConfirmPasswordChange = {},
      onNameChange = {},
      onSubmitClick = {},
      onNavigateBack = {}
    )
  }
}

@Preview
@Composable
private fun RegisterFormWithErrorsPreview() {
  DexReaderTheme {
    RegisterForm(
      email = "invalid-email",
      emailError = UserError.Email.Invalid,
      password = "123",
      passwordError = UserError.Password.Weak,
      confirmPassword = "456",
      confirmPasswordError = UserError.ConfirmPassword.DoesNotMatch,
      name = "",
      nameError = UserError.Name.Required,
      modifier = Modifier.fillMaxSize(),
      onEmailChange = {},
      onPasswordChange = {},
      onConfirmPasswordChange = {},
      onNameChange = {},
      onSubmitClick = {},
      onNavigateBack = {}
    )
  }
}
