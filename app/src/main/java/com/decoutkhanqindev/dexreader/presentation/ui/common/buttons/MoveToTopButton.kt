package com.decoutkhanqindev.dexreader.presentation.ui.common.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.dexreader.R

@Composable
fun MoveToTopButton(
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
      imageVector = Icons.Filled.ArrowUpward,
      contentDescription = stringResource(R.string.move_to_top),
    )
  }
}