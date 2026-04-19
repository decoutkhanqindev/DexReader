package com.decoutkhanqindev.dexreader.presentation.screens.auth

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.presentation.error.UserError

private val NameKeyboardOptions = KeyboardOptions(
  keyboardType = KeyboardType.Text,
  imeAction = ImeAction.Next,
)

@Composable
fun NameInputField(
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
    modifier = modifier,
    leadingIcon = {
      Icon(
        imageVector = Icons.Default.Person,
        contentDescription = stringResource(R.string.name),
        modifier = Modifier.size(24.dp),
        tint = colorScheme.onPrimaryContainer,
      )
    },
    label = {
      Text(
        text = stringResource(R.string.name),
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
    keyboardOptions = NameKeyboardOptions,
    colors = colors,
  )
}

@Preview
@Composable
private fun NameInputFieldPreview() {
  NameInputField(
    value = "Nguyen Van A",
    modifier = Modifier.fillMaxWidth(),
    onValueChange = {}
  )
}

@Preview
@Composable
private fun NameInputFieldErrorPreview() {
  NameInputField(
    value = "",
    error = UserError.Name.Required,
    modifier = Modifier.fillMaxWidth(),
    onValueChange = {}
  )
}
