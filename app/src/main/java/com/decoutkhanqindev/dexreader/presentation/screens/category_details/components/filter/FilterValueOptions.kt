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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.screens.category_details.FilterValue

@Composable
fun FilterValueOptions(
  selectedValueIds: List<String>,
  filterValueOptions: List<FilterValue>,
  onSelectedOptions: (List<String>) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(space = 16.dp),
  ) {
    filterValueOptions.forEach { option ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .selectable(
            selected = selectedValueIds.contains(option.id),
            onClick = {
              if (selectedValueIds.contains(option.id))
                onSelectedOptions(selectedValueIds - option.id)
              else onSelectedOptions(selectedValueIds + option.id)
            },
            role = Role.Checkbox
          ),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
      ) {
        Checkbox(
          checked = selectedValueIds.contains(option.id),
          onCheckedChange = null
        )
        Text(
          text = option.name,
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.Bold,
        )
      }
    }
  }
}