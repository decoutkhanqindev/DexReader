package com.decoutkhanqindev.dexreader.presentation.screens.splash.com

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.splash.SplashLogonAndSlogan

@Composable
fun SplashContent(modifier: Modifier = Modifier) {
  Box(modifier = modifier) {
    SplashLogonAndSlogan(modifier = Modifier.align(Alignment.TopCenter))

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.BottomCenter),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = stringResource(R.string.loading),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
      )
      LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
      )
    }
  }
}