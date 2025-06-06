package com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.actions

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
      Text(
        text = stringResource(R.string.sort),
        color = MaterialTheme.colorScheme.inverseSurface,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.ExtraBold,
      )
    },
    modifier = modifier
  )
}