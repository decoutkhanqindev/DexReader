package com.decoutkhanqindev.dexreader.presentation.screens.categories.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel

@Composable
fun CategoryItem(
  item: CategoryModel,
  modifier: Modifier = Modifier,
  onClick: (String, String) -> Unit,
) {
  Card(
    onClick = { onClick(item.id, item.title) },
    modifier = modifier,
    shape = MaterialTheme.shapes.large,
  ) {
    Text(
      text = item.title,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .padding(8.dp)
        .wrapContentWidth(),
      style = MaterialTheme.typography.bodyLarge,
    )
  }
}