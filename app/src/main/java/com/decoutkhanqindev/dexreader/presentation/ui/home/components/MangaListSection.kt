package com.decoutkhanqindev.dexreader.presentation.ui.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.presentation.ui.common.lists.HorizontalMangaList


@Composable
fun MangaListSection(
  title: String,
  mangaList: List<Manga>,
  onSelectedManga: (Manga) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.ExtraBold,
      color = MaterialTheme.colorScheme.onPrimaryContainer,
      modifier = Modifier.padding(
        start = 16.dp,
        top = 8.dp,
        bottom = 4.dp
      )
    )
    HorizontalMangaList(
      mangaList = mangaList,
      onSelectedManga = onSelectedManga,
      modifier = Modifier.fillMaxWidth()
    )
  }
}