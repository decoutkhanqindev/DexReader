package com.decoutkhanqindev.dexreader.presentation.ui.category_details.components.filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.FilterCriteria
import com.decoutkhanqindev.dexreader.presentation.ui.category_details.FilterValue

@Composable
fun FilterCriteriaItem(
  criteria: FilterCriteria,
  selectedValueIds: List<String>,
  filterValueOptions: List<FilterValue>,
  onSelectedOptions: (List<String>) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ),
    elevation = CardDefaults.cardElevation(4.dp),
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
    ) {
      Text(
        text = criteria.name,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp)
      )
      FilterValueOptions(
        selectedValueIds = selectedValueIds,
        filterValueOptions = filterValueOptions,
        onSelectedOptions = onSelectedOptions,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}