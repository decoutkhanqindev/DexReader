package com.decoutkhanqindev.dexreader.presentation.ui.categories.components

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
import com.decoutkhanqindev.dexreader.domain.model.Category

@Composable
fun CategoryItem(
  category: Category,
  onSelectedCategory: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    onClick = { onSelectedCategory(category.id, category.title) },
    shape = MaterialTheme.shapes.large,
    modifier = modifier,
  ) {
    Text(
      text = category.title,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .padding(8.dp)
        .wrapContentWidth()
    )
  }
}