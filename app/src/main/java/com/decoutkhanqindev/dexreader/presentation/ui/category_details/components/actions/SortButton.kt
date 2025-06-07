package com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.actions

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.common.buttons.ActionButton

@Composable
fun SortButton(
  onSortClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  ActionButton(
    onClick = onSortClick,
    content = {
      Icon(
        imageVector = Icons.AutoMirrored.Filled.Sort,
        contentDescription = stringResource(R.string.sort),
        tint = MaterialTheme.colorScheme.onPrimaryContainer,
      )
    },
    modifier = modifier.size(48.dp)
  )
}