package com.decoutkhanqindev.dexreader.presentation.screens.auth.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.error.UserError
import com.decoutkhanqindev.dexreader.presentation.screens.auth.EmailInputField
import com.decoutkhanqindev.dexreader.presentation.screens.auth.PasswordInputField
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.SubmitButton

@Composable
fun LoginForm(
  email: String,
  emailError: UserError?,
  password: String,
  passwordError: UserError?,
  modifier: Modifier = Modifier,
  onEmailChange: (String) -> Unit,
  onPasswordChange: (String) -> Unit,
  onSubmitClick: () -> Unit,
  onRegisterClick: () -> Unit,
  onForgotPasswordClick: () -> Unit,
) {
  val scrollState = rememberScrollState()

  Card(
    modifier = modifier.verticalScroll(scrollState),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = stringResource(R.string.sign_in),
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

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 16.dp),
      ) {
        PasswordInputField(
          value = password,
          error = passwordError,
          modifier = Modifier.fillMaxWidth(),
        ) { onPasswordChange(it) }
        Text(
          text = stringResource(R.string.forgot_password),
          modifier = Modifier
            .align(Alignment.End)
            .minimumInteractiveComponentSize()
            .clickable(onClick = onForgotPasswordClick),
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          fontWeight = FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          style = MaterialTheme.typography.titleMedium,
        )
      }

      SubmitButton(
        title = stringResource(R.string.sign_in),
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp),
      ) { onSubmitClick() }

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          text = stringResource(R.string.don_t_have_an_account),
          modifier = Modifier.padding(end = 4.dp),
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.Light,
          style = MaterialTheme.typography.titleMedium,
        )
        Text(
          text = stringResource(R.string.sign_up),
          modifier = Modifier
            .minimumInteractiveComponentSize()
            .clickable(onClick = onRegisterClick),
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          fontWeight = FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          style = MaterialTheme.typography.titleMedium,
        )
      }
    }
  }
}

@Preview
@Composable
private fun LoginFormPreview() {
  LoginForm(
    email = "nguyenvana@gmail.com",
    emailError = null,
    password = "myPassword123",
    passwordError = null,
    modifier = Modifier.fillMaxSize(),
    onEmailChange = {},
    onPasswordChange = {},
    onSubmitClick = {},
    onRegisterClick = {},
    onForgotPasswordClick = {}
  )
}

@Preview
@Composable
private fun LoginFormWithErrorsPreview() {
  LoginForm(
    email = "invalid-email",
    emailError = UserError.Email.Invalid,
    password = "123",
    passwordError = UserError.Password.Weak,
    modifier = Modifier.fillMaxSize(),
    onEmailChange = {},
    onPasswordChange = {},
    onSubmitClick = {},
    onRegisterClick = {},
    onForgotPasswordClick = {}
  )
}
