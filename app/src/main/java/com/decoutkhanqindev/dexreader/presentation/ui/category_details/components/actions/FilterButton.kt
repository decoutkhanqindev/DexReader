package com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.actions

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.ui.common.buttons.ActionButton

@Composable
fun FilterButton(
  onFilterClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  ActionButton(
    onClick = onFilterClick,
    content = {
      Icon(
        imageVector = Icons.Default.FilterAlt,
        contentDescription = stringResource(R.string.filter),
        tint = MaterialTheme.colorScheme.onPrimaryContainer,
      )
    },
    modifier = modifier.size(48.dp)
  )
}