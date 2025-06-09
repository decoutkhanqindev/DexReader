package com.decoutkhanqindev.dexreader.presentation.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthContent(
  content: @Composable () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.background(MaterialTheme.colorScheme.surface)
  ) {
    Box(
      modifier = Modifier
        .weight(0.25f)
        .fillMaxWidth(),
      contentAlignment = Alignment.Center
    ) {
      AuthHeader(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .align(Alignment.Center)
      )
    }

    Box(
      modifier = Modifier
        .weight(0.75f)
        .fillMaxWidth()
    ) { content() }
  }
}