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

@Composable
fun <T> FilterValueOptions(
  options: List<T>,
  selectedOptions: List<T>,
  nameResOf: (T) -> Int,
  onSelectedOptions: (List<T>) -> Unit,
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
            selected = selectedOptions.contains(option),
            onClick = {
              if (selectedOptions.contains(option))
                onSelectedOptions(selectedOptions - option)
              else onSelectedOptions(selectedOptions + option)
            },
            role = Role.Checkbox
          ),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
      ) {
        Checkbox(
          checked = selectedOptions.contains(option),
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
