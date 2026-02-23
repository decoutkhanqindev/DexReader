package com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.auth.EmailInputField
import com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password.ForgotPasswordUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.SubmitButton

@Composable
fun ForgotPasswordForm(
  uiState: ForgotPasswordUiState,
  onEmailChange: (String) -> Unit,
  onSubmitClick: () -> Unit,
  onNavigateBack: () -> Unit,
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
        text = stringResource(R.string.forgot_password),
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
        error = stringResource(uiState.emailError.message),
        modifier = Modifier.fillMaxWidth()
      )

      SubmitButton(
        title = stringResource(R.string.reset_password),
        onSubmitClick = {
          onSubmitClick()
          isShowError = true
        },
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp)
      )

      ActionButton(
        onClick = onNavigateBack,
        content = {
          Text(
            text = stringResource(R.string.back),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.inverseSurface,
            fontWeight = FontWeight.ExtraBold,
          )
        },
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}