package com.decoutkhanqindev.dexreader.presentation.screens.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.error.UserError
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun PasswordInputField(
  value: String,
  isConfirmed: Boolean = false,
  error: UserError? = null,
  modifier: Modifier = Modifier,
  onValueChange: (String) -> Unit,
) {
  val keyboardOptions = remember {
    KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next)
  }
  val passwordTransformation = remember { PasswordVisualTransformation() }
  var isShowPassword by remember { mutableStateOf(false) }

  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    leadingIcon = {
      Icon(
        imageVector = Icons.Default.Lock,
        contentDescription =
          if (isConfirmed) stringResource(R.string.confirm_password)
          else stringResource(R.string.password),
        modifier = Modifier.size(24.dp),
        tint = MaterialTheme.colorScheme.onPrimaryContainer,
      )
    },
    label = {
      Text(
        text =
          if (isConfirmed) stringResource(R.string.confirm_password)
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
          modifier = Modifier.size(24.dp),
          tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )
      }
    },
    singleLine = true,
    isError = error != null,
    supportingText = {
      error?.let {
        Text(
          text = stringResource(it.messageRes),
          color = MaterialTheme.colorScheme.error
        )
      }
    },
    textStyle = MaterialTheme.typography.bodyLarge,
    visualTransformation =
      if (isShowPassword) VisualTransformation.None
      else passwordTransformation,
    keyboardOptions = keyboardOptions,
    colors = OutlinedTextFieldDefaults.colors(
      focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
      unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
      focusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
      unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
      cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
      focusedTextColor = MaterialTheme.colorScheme.onSurface,
      unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
      focusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
      unfocusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ),
  )
}

@Preview
@Composable
private fun PasswordInputFieldPreview() {
  DexReaderTheme {
    PasswordInputField(
      value = "myPassword123",
      modifier = Modifier.fillMaxWidth(),
      onValueChange = {}
    )
  }
}

@Preview
@Composable
private fun PasswordInputFieldConfirmedPreview() {
  DexReaderTheme {
    PasswordInputField(
      value = "myPassword123",
      isConfirmed = true,
      modifier = Modifier.fillMaxWidth(),
      onValueChange = {}
    )
  }
}

@Preview
@Composable
private fun PasswordInputFieldErrorPreview() {
  DexReaderTheme {
    PasswordInputField(
      value = "123",
      error = UserError.Password.Weak,
      modifier = Modifier.fillMaxWidth(),
      onValueChange = {}
    )
  }
}

@Preview
@Composable
private fun PasswordInputFieldMismatchPreview() {
  DexReaderTheme {
    PasswordInputField(
      value = "differentPassword",
      isConfirmed = true,
      error = UserError.ConfirmPassword.DoesNotMatch,
      modifier = Modifier.fillMaxWidth(),
      onValueChange = {}
    )
  }
}
