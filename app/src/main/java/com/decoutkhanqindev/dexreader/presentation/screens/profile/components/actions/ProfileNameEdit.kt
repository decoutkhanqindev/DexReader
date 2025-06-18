package com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ProfileNameEdit(
  name: String,
  onNameChange: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var isEdit by rememberSaveable { mutableStateOf(false) }
  val focusRequester = remember{ FocusRequester() }
  val focusManager = LocalFocusManager.current

  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
  ) {
    if (!isEdit) {
      Text(
        text = name,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center,
      )
    } else {
      TextField(
        value = name,
        onValueChange = onNameChange,
        textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
        singleLine = true,
        colors = TextFieldDefaults.colors(
          focusedContainerColor = MaterialTheme.colorScheme.surface,
          unfocusedContainerColor = MaterialTheme.colorScheme.surface,
          focusedIndicatorColor = MaterialTheme.colorScheme.surface,
          unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
          cursorColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
          imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
          onDone = {
            isEdit = false
            focusManager.clearFocus()
          }
        ),
        modifier = Modifier.focusRequester(focusRequester),
      )

      LaunchedEffect(isEdit) {
        if (isEdit) focusRequester.requestFocus()
      }
    }
    IconButton(
      onClick = { isEdit = !isEdit },
      modifier = Modifier
    ) {
      Icon(
        imageVector = if (!isEdit) Icons.Default.Edit else Icons.Default.Done,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.size(24.dp)
      )
    }
  }
}