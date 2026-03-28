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
  modifier: Modifier = Modifier,
  onClick: (MenuItemValue) -> Unit,
) {
  val title = stringResource(item.nameRes)

  NavigationDrawerItem(
    label = {
      Text(
        text = title,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge,
      )
    },
    selected = isSelected,
    onClick = { onClick(item) },
    modifier = modifier,
    icon = {
      Icon(
        imageVector = item.icon,
        contentDescription = title,
        tint = MaterialTheme.colorScheme.onPrimaryContainer
      )
    },
    shape = RoundedCornerShape(0.dp),
  )
}
