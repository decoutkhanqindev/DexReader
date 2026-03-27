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
import com.decoutkhanqindev.dexreader.presentation.error.UserError

private val PasswordKeyboardOptions = KeyboardOptions(
  keyboardType = KeyboardType.Password,
  imeAction = ImeAction.Next,
)

private val PasswordTransformation = PasswordVisualTransformation()

@Composable
fun PasswordInputField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  isConfirmed: Boolean = false,
  error: UserError? = null,
) {
  var isShowPassword by rememberSaveable { mutableStateOf(false) }
  // OutlinedTextFieldDefaults.colors() is @Composable so it cannot be wrapped in remember {}.
  // Consolidating into a single colorScheme read avoids repeated MaterialTheme.colorScheme lookups.
  val colorScheme = MaterialTheme.colorScheme
  val colors = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = colorScheme.surfaceContainer,
    unfocusedContainerColor = colorScheme.surfaceContainer,
    focusedBorderColor = colorScheme.onPrimaryContainer,
    unfocusedBorderColor = colorScheme.onPrimaryContainer,
    cursorColor = colorScheme.onPrimaryContainer,
    focusedTextColor = colorScheme.onSurface,
    unfocusedTextColor = colorScheme.onSurface,
    focusedLabelColor = colorScheme.onPrimaryContainer,
    unfocusedLabelColor = colorScheme.onPrimaryContainer,
  )

  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    leadingIcon = {
      Icon(
        imageVector = Icons.Default.Lock,
        contentDescription =
          if (isConfirmed) stringResource(R.string.confirm_password)
          else stringResource(R.string.password),
        tint = colorScheme.onPrimaryContainer,
        modifier = Modifier.size(24.dp)
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
          tint = colorScheme.onPrimaryContainer,
          modifier = Modifier.size(24.dp)
        )
      }
    },
    singleLine = true,
    isError = error != null,
    supportingText = {
      error?.let {
        Text(
          text = stringResource(it.messageRes),
          color = colorScheme.error
        )
      }
    },
    textStyle = MaterialTheme.typography.bodyLarge,
    visualTransformation =
      if (isShowPassword) VisualTransformation.None
      else PasswordTransformation,
    keyboardOptions = PasswordKeyboardOptions,
    colors = colors,
    modifier = modifier,
  )
}
