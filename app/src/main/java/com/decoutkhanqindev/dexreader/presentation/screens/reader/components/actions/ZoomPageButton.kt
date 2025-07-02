package com.decoutkhanqindev.dexreader.presentation.screens.reader.components.actions

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R

@Composable
fun ZoomPageButton(
  isFullScreen: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  FloatingActionButton(
    onClick = onClick,
    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
    contentColor = MaterialTheme.colorScheme.surfaceContainer,
    modifier = modifier
  ) {
    Icon(
      imageVector =
        if (isFullScreen) Icons.Filled.FullscreenExit
        else Icons.Filled.Fullscreen,
      contentDescription =
        if (isFullScreen) stringResource(R.string.exit_fullscreen)
        else stringResource(R.string.fullscreen),
      modifier = Modifier.padding(16.dp)
    )
  }
}