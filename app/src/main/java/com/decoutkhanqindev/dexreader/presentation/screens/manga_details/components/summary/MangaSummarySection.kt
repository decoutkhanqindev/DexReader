package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel


@Composable
fun MangaSummarySection(
  manga: MangaModel,
  modifier: Modifier = Modifier,
  onSelectedCategory: (
    categoryId: String,
    categoryTitle: String,
  ) -> Unit,
) {
  Column(modifier = modifier) {
    Text(
      text = stringResource(R.string.summary),
      modifier = Modifier
        .padding(horizontal = 4.dp)
        .padding(bottom = 12.dp),
      fontWeight = FontWeight.ExtraBold,
      style = MaterialTheme.typography.titleLarge,
    )
    MangaDescription(
      description = manga.description,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp)
    )
    MangaCategoryList(
      categoryList = manga.categories,
      modifier = Modifier.fillMaxWidth(),
    ) { categoryId, categoryTitle -> onSelectedCategory(categoryId, categoryTitle) }
  }
}
