package com.decoutkhanqindev.dexreader.presentation.screens.auth

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R

@Composable
fun PasswordInputField(
  isConfirmPassword: Boolean = false,
  password: String,
  isValidPassword: Boolean = true,
  onPasswordChange: (String) -> Unit,
  isShowError: Boolean = false,
  error: String,
  modifier: Modifier = Modifier
) {
  var isShowPassword by rememberSaveable { mutableStateOf(false) }

  OutlinedTextField(
    value = password,
    onValueChange = onPasswordChange,
    leadingIcon = {
      Icon(
        imageVector = Icons.Default.Lock,
        contentDescription =
          if (isConfirmPassword) stringResource(R.string.confirm_password)
          else stringResource(R.string.password),
        tint = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.size(24.dp)
      )
    },
    label = {
      Text(
        text =
          if (isConfirmPassword) stringResource(R.string.confirm_password)
          else stringResource(R.string.password),
        style = MaterialTheme.typography.bodyLarge
      )
    },
    trailingIcon = {
      IconButton(onClick = { isShowPassword = !isShowPassword }) {
        Icon(
          imageVector =
            if (isShowPassword) Icons.Default.Visibility
            else Icons.Default.VisibilityOff,
          contentDescription =
            if (isShowPassword) stringResource(R.string.hide_password)
            else stringResource(R.string.show_password),
          tint = MaterialTheme.colorScheme.onPrimaryContainer,
          modifier = Modifier.size(24.dp)
        )
      }
    },
    singleLine = true,
    isError = isShowError && !isValidPassword,
    supportingText = {
      if (isShowError && !isValidPassword) {
        Text(
          text = error,
          color = MaterialTheme.colorScheme.error
        )
      }
    },
    textStyle = MaterialTheme.typography.bodyLarge,
    visualTransformation =
      if (isShowPassword) VisualTransformation.None
      else PasswordVisualTransformation(),
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.Password,
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
    modifier = modifier
  )
}

