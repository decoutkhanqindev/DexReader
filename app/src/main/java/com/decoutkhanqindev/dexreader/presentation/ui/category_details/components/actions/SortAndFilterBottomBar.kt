package com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.actions

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.common.buttons.ActionButton

@Composable
fun SortAndFilterBottomBar(
  onSortClick: () -> Unit,
  onFilterClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  BottomAppBar(
    actions = {
      ActionButton(
        onClick = onSortClick,
        content = {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.Sort,
            contentDescription = stringResource(R.string.sort),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
          )
        },
        modifier = Modifier
          .weight(1f)
          .size(48.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      ActionButton(
        onClick = onFilterClick,
        content = {
          Icon(
            imageVector = Icons.Default.FilterAlt,
            contentDescription = stringResource(R.string.filter),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
          )
        },
        modifier = Modifier
          .weight(1f)
          .size(48.dp)
      )
    },
    containerColor = MaterialTheme.colorScheme.surfaceContainer,
    modifier = modifier
  )
}