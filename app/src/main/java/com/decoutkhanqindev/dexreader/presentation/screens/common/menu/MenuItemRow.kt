package com.decoutkhanqindev.dexreader.presentation.screens.common.menu

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuItemValue

@Composable
fun MenuItemRow(
  isSelected: Boolean,
  item: MenuItemValue,
  onItemClick: (MenuItemValue) -> Unit,
  modifier: Modifier = Modifier,
) {
  val title = stringResource(item.nameRes)

  NavigationDrawerItem(
    selected = isSelected,
    onClick = { onItemClick(item) },
    label = {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
      )
    },
    icon = {
      Icon(
        imageVector = item.icon,
        contentDescription = title,
        tint = MaterialTheme.colorScheme.onPrimaryContainer
      )
    },
    shape = RoundedCornerShape(0.dp),
    modifier = modifier,
  )
}
