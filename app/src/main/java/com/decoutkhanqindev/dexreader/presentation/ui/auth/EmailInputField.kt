package com.decoutkhanqindev.dexreader.presentation.ui.auth

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R

@Composable
fun EmailInputField(
  email: String,
  isValidEmail: Boolean = true,
  onEmailChange: (String) -> Unit,
  isShowError: Boolean = false,
  error: String,
  modifier: Modifier = Modifier
) {
  OutlinedTextField(
    value = email,
    onValueChange = onEmailChange,
    leadingIcon = {
      Icon(
        imageVector = Icons.Default.Email,
        contentDescription = stringResource(R.string.email),
        tint = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.size(24.dp)
      )
    },
    label = {
      Text(
        text = stringResource(R.string.email),
        style = MaterialTheme.typography.bodyLarge
      )
    },
    singleLine = true,
    isError = isShowError && !isValidEmail,
    supportingText = {
      if (isShowError && !isValidEmail) {
        Text(
          text = error,
          color = MaterialTheme.colorScheme.error
        )
      }
    },
    textStyle = MaterialTheme.typography.bodyLarge,
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.Email,
      imeAction = ImeAction.Next
    ),
    colors = OutlinedTextFieldDefaults.colors(
      focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
      unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
      focusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
      unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
      cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
      focusedTextColor = MaterialTheme.colorScheme.onSurface,
      unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
      focusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
      unfocusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
    ),
    modifier = modifier,
  )
}