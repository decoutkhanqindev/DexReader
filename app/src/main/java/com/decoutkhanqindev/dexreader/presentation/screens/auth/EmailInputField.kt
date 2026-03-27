package com.decoutkhanqindev.dexreader.presentation.screens.auth

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
import com.decoutkhanqindev.dexreader.presentation.error.UserError

private val EmailKeyboardOptions = KeyboardOptions(
  keyboardType = KeyboardType.Email,
  imeAction = ImeAction.Next,
)

@Composable
fun EmailInputField(
  value: String,
  error: UserError? = null,
  modifier: Modifier = Modifier,
  onValueChange: (String) -> Unit,
) {
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
        imageVector = Icons.Default.Email,
        contentDescription = stringResource(R.string.email),
        tint = colorScheme.onPrimaryContainer,
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
    keyboardOptions = EmailKeyboardOptions,
    colors = colors,
    modifier = modifier,
  )
}
