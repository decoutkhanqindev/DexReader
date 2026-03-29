package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.summary

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
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import java.util.Locale

@Composable
fun MangaCategoryItem(
  item: CategoryModel,
  modifier: Modifier = Modifier,
  onClick: (
    categoryId: String,
    categoryTitle: String,
  ) -> Unit,
) {
  Card(
    onClick = {
      onClick(
        item.id,
        item.title
      )
    },
    modifier = modifier,
    shape = MaterialTheme.shapes.large,
  ) {
    Text(
      text = item.title.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.US)
        else it.toString()
      },
      modifier = Modifier
        .padding(8.dp)
        .fillMaxSize(),
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.bodyLarge,
    )
  }
}