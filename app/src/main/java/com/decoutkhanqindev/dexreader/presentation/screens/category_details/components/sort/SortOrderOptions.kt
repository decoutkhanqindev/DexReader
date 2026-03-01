package com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.sort

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.criteria.sort.MangaSortOrderOption

@Composable
fun SortOrderOptions(
  selectedOrder: MangaSortOrderOption,
  onSelectedOption: (MangaSortOrderOption) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    MangaSortOrderOption.entries.forEach { option ->
      Row(
        modifier = Modifier
          .weight(1f)
          .selectable(
            selected = selectedOrder == option,
            onClick = { onSelectedOption(option) },
            role = Role.RadioButton
          ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
          space = 8.dp,
          alignment = Alignment.CenterHorizontally
        )
      ) {
        RadioButton(
          selected = selectedOrder == option,
          onClick = null
        )
        Text(
          text = stringResource(option.nameRes),
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.Bold,
        )
      }
    }
  }
}
