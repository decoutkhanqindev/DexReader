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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.auth.EmailInputField
import com.decoutkhanqindev.dexreader.presentation.screens.auth.PasswordInputField
import com.decoutkhanqindev.dexreader.presentation.screens.auth.login.LoginUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.SubmitButton

@Composable
fun LoginForm(
  uiState: LoginUiState,
  onEmailChange: (String) -> Unit,
  onPasswordChange: (String) -> Unit,
  onSubmitClick: () -> Unit,
  onRegisterClick: () -> Unit,
  onForgotPasswordClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var isShowError by rememberSaveable { mutableStateOf(false) }
  val scrollState = rememberScrollState()

  Card(
    modifier = modifier.verticalScroll(scrollState),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
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
        text = stringResource(R.string.sign_in),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp)
      )

      Spacer(modifier = Modifier.height(16.dp))

      EmailInputField(
        email = uiState.email,
        isValidEmail = uiState.isValidEmail,
        onEmailChange = { onEmailChange(it) },
        isShowError = isShowError,
        error = uiState.emailError.message,
        modifier = Modifier.fillMaxWidth()
      )

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 16.dp),
      ) {
        PasswordInputField(
          password = uiState.password,
          isValidPassword = uiState.isValidPassword,
          onPasswordChange = { onPasswordChange(it) },
          isShowError = isShowError,
          error = uiState.passwordError.message,
          modifier = Modifier.fillMaxWidth()
        )
        Text(
          text = stringResource(R.string.forgot_password),
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          fontWeight = FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          modifier = Modifier
            .align(Alignment.End)
            .clickable { onForgotPasswordClick() }
        )
      }

      SubmitButton(
        title = stringResource(R.string.sign_in),
        onSubmitClick = {
          onSubmitClick()
          isShowError = true
        },
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp)
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          text = stringResource(R.string.don_t_have_an_account),
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.Light,
          modifier = Modifier.padding(end = 4.dp)
        )
        Text(
          text = stringResource(R.string.sign_up),
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          fontWeight = FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          modifier = Modifier.clickable { onRegisterClick() }
        )
      }
    }
  }
}