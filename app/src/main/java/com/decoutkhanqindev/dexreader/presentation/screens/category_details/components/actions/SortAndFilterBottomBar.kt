package com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.actions

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun SortAndFilterBottomBar(
  onSortClick: () -> Unit,
  onFilterClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  BottomAppBar(
    actions = {
      ActionButton(
        modifier = Modifier.weight(1f),
        onClick = onSortClick
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.Sort,
          contentDescription = stringResource(R.string.sort),
          tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      ActionButton(
        modifier = Modifier.weight(1f),
        onClick = onFilterClick
      ) {
        Icon(
          imageVector = Icons.Default.FilterAlt,
          contentDescription = stringResource(R.string.filter),
          tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )
      }
    },
    containerColor = MaterialTheme.colorScheme.surface,
    modifier = modifier
  )
}

@Preview
@Composable
private fun SortAndFilterBottomBarPreview() {
  DexReaderTheme {
    SortAndFilterBottomBar(
      onSortClick = {},
      onFilterClick = {}
    )
  }
}