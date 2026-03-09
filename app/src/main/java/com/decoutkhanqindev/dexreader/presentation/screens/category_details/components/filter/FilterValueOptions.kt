package com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.filter


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun <T> FilterValueOptions(
  options: ImmutableList<T>,
  selectedItems: ImmutableList<T>,
  nameResOf: (T) -> Int,
  onItemsSelect: (ImmutableList<T>) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(space = 16.dp),
  ) {
    options.forEach { option ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .selectable(
            selected = selectedItems.contains(option),
            onClick = {
              if (selectedItems.contains(option))
                onItemsSelect(selectedItems.toPersistentList().remove(option))
              else onItemsSelect(selectedItems.toPersistentList().add(option))
            },
            role = Role.Checkbox
          ),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
      ) {
        Checkbox(
          checked = selectedItems.contains(option),
          onCheckedChange = null
        )
        Text(
          text = stringResource(nameResOf(option)),
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.Bold,
        )
      }
    }
  }
}
