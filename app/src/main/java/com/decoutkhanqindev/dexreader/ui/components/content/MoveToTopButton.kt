package com.decoutkhanqindev.dexreader.ui.components.content

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
    modifier = modifier
  ) {
    Icon(
      imageVector = Icons.Default.KeyboardArrowUp,
      contentDescription = stringResource(R.string.move_to_top),
    )
  }
}