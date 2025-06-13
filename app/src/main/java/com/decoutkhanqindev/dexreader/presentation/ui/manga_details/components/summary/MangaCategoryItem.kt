package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.summary

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.domain.model.Category
import java.util.Locale

@Composable
fun MangaCategoryItem(
  category: Category,
  onSelectedCategory: (
    categoryId: String,
    categoryTitle: String
  ) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = MaterialTheme.shapes.large,
    onClick = {
      onSelectedCategory(
        category.id,
        category.title
      )
    }
  ) {
    Text(
      text = category.title.capitalize(Locale.US),
      style = MaterialTheme.typography.bodyLarge,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .padding(8.dp)
        .fillMaxSize()
    )
  }
}