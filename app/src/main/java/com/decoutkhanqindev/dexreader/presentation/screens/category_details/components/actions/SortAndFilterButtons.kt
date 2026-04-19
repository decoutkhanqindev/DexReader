package com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun SortAndFilterButtons(
  onSortClick: () -> Unit,
  onFilterClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
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
  }
}

@Preview
@Composable
private fun SortAndFilterButtonsPreview() {
  DexReaderTheme {
    SortAndFilterButtons(
      onSortClick = {},
      onFilterClick = {}
    )
  }
}