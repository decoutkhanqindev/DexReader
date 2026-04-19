package com.decoutkhanqindev.dexreader.presentation.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.R

@Composable
fun AuthHeader(modifier: Modifier = Modifier) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(
      space = 16.dp,
      alignment = Alignment.CenterHorizontally
    )
  ) {
    Icon(
      painter = painterResource(R.drawable.app_icon),
      contentDescription = stringResource(R.string.app_name),
      modifier = Modifier.size(100.dp),
      tint = MaterialTheme.colorScheme.onPrimaryContainer,
    )
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = stringResource(R.string.app_name),
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        fontWeight = FontWeight.ExtraBold,
        style = MaterialTheme.typography.headlineLarge,
      )
      Text(
        text = stringResource(R.string.slogan),
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Light,
        style = MaterialTheme.typography.titleMedium,
      )
    }
  }
}

@Preview
@Composable
private fun AuthHeaderPreview() {
  AuthHeader(modifier = Modifier.fillMaxWidth().padding(16.dp))
}