package com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.filter


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
import kotlinx.collections.immutable.ImmutableList

@Composable
fun <T> FilterCriteriaItem(
  title: String,
  items: ImmutableList<T>,
  selectedItems: ImmutableList<T>,
  nameResOf: (T) -> Int,
  onItemsSelect: (ImmutableList<T>) -> Unit,
  modifier: Modifier = Modifier,
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
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp)
      )
      FilterValueOptions(
        options = items,
        selectedItems = selectedItems,
        nameResOf = nameResOf,
        onItemsSelect = onItemsSelect,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}
