package com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun SignInButton(
  modifier: Modifier = Modifier,
  onSignInClick: () -> Unit,
) {
  ActionButton(
    isHighlighted = true,
    backgroundColor = MaterialTheme.colorScheme.primary,
    modifier = modifier,
    onClick = onSignInClick
  ) {
    Text(
      text = stringResource(R.string.sign_in),
      color = MaterialTheme.colorScheme.onPrimary,
      fontWeight = FontWeight.ExtraBold,
      style = MaterialTheme.typography.titleMedium,
    )

    Icon(
      imageVector = Icons.AutoMirrored.Filled.Login,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onPrimary,
      modifier = Modifier
        .size(24.dp)
        .padding(start = 8.dp)
    )
  }
}

@Preview
@Composable
private fun SignInButtonPreview() {
  DexReaderTheme {
    SignInButton(
      modifier = Modifier.fillMaxWidth(),
      onSignInClick = {}
    )
  }
}
