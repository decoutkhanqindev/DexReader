package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.summary

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
import com.decoutkhanqindev.dexreader.domain.model.Manga


@Composable
fun MangaSummarySection(
  manga: Manga,
  onSelectedCategory: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    Text(
      text = stringResource(R.string.summary),
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.ExtraBold,
      modifier = Modifier
        .padding(horizontal = 4.dp)
        .padding(bottom = 12.dp)
    )
    MangaDescription(
      description = manga.description,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp)
    )
    MangaCategoryList(
      categoryList = manga.categories,
      onSelectedCategory = onSelectedCategory,
      modifier = Modifier.fillMaxWidth()
    )
  }
}
