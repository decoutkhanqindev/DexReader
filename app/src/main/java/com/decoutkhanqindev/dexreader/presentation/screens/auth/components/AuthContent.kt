package com.decoutkhanqindev.dexreader.presentation.screens.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.screens.common.animation.AnimatedLogoAndSlogan
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun AuthContent(
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit,
) {
  Column(
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier.background(MaterialTheme.colorScheme.surface)
  ) {
    AnimatedLogoAndSlogan(
      modifier = Modifier
        .weight(0.3f)
        .fillMaxWidth()
        .padding(16.dp)
        .statusBarsPadding()
    )

    Box(
      modifier = Modifier
        .weight(0.7f)
        .fillMaxWidth()
    ) { content() }
  }
}

@Preview
@Composable
private fun AuthContentPreview() {
  DexReaderTheme {
    AuthContent(modifier = Modifier.fillMaxSize()) {
      Text(text = "Content goes here")
    }
  }
}