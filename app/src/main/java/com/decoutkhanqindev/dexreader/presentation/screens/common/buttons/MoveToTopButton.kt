package com.decoutkhanqindev.dexreader.presentation.screens.common.buttons

import android.R.attr.onClick
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun MoveToTopButton(
  itemsSize: Int,
  listState: LazyListState? = null,
  gridState: LazyGridState? = null,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  val isVisible by remember {
    derivedStateOf {
      if (listState != null) {
        listState.firstVisibleItemIndex > 0 && itemsSize > 15
      } else if (gridState != null) {
        gridState.firstVisibleItemIndex > 0 && itemsSize > 15
      } else {
        false
      }
    }
  }

  AnimatedVisibility(
    visible = isVisible,
    enter = scaleIn(),
    exit = scaleOut(),
    modifier = modifier
  ) {
    FloatingActionButton(
      onClick = onClick,
      modifier = Modifier.size(48.dp),
      shape = CircleShape,
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = MaterialTheme.colorScheme.inverseSurface,
    ) {
      Icon(
        imageVector = Icons.Filled.ArrowUpward,
        contentDescription = stringResource(R.string.move_to_top),
      )
    }
  }
}

@Preview
@Composable
private fun MoveToTopButtonVisiblePreview() {
  DexReaderTheme {
    MoveToTopButton(
      itemsSize = 20,
      onClick = {}
    )
  }
}

@Preview
@Composable
private fun MoveToTopButtonHiddenPreview() {
  DexReaderTheme {
    MoveToTopButton(
      itemsSize = 5,
      onClick = {}
    )
  }
}