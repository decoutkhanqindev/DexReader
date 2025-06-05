package com.decoutkhanqindev.dexreader.presentation.ui.common.menu

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MenuItemRow(
  isSelected: Boolean,
  item: MenuItem,
  onItemClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationDrawerItem(
    selected = isSelected,
    onClick = { onItemClick(item.id) },
    label = {
      Text(
        text = item.title,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
      )
    },
    icon = {
      Icon(
        imageVector = item.icon,
        contentDescription = item.title,
        tint = MaterialTheme.colorScheme.onPrimaryContainer
      )
    },
    shape = RoundedCornerShape(0.dp),
    modifier = modifier,
  )
}
