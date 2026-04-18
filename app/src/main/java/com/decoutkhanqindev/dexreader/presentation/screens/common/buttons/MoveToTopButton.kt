package com.decoutkhanqindev.dexreader.presentation.screens.common.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R

@Composable
fun MoveToTopButton(
  itemsSize: Int,
  firstVisibleItemIndex: Int,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  val isVisible by remember(itemsSize, firstVisibleItemIndex) {
    derivedStateOf { itemsSize > 15 && firstVisibleItemIndex > 0 }
  }

  AnimatedVisibility(
    visible = isVisible,
    enter = scaleIn(),
    exit = scaleOut(),
    modifier = modifier
  ) {
    FloatingActionButton(
      onClick = onClick,
      modifier = Modifier
        .size(48.dp)
        .border(
          width = 1.dp,
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          shape = CircleShape
        ),
      shape = CircleShape,
      containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
      contentColor =  MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
      Icon(
        imageVector = Icons.Filled.ArrowUpward,
        contentDescription = stringResource(R.string.move_to_top),
        tint = Color.White
      )
    }
  }
}