package com.decoutkhanqindev.dexreader.presentation.screens.home.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.lists.manga.HorizontalMangaList
import kotlinx.collections.immutable.ImmutableList


@Composable
fun MangaListSection(
  title: String,
  mangaList: ImmutableList<MangaModel>,
  onSelectedManga: (MangaModel) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.ExtraBold,
      modifier = Modifier.padding(
        start = 16.dp,
        top = 8.dp,
        bottom = 4.dp
      )
    )
    HorizontalMangaList(
      items = mangaList,
      onItemClick = onSelectedManga,
      modifier = Modifier.fillMaxWidth()
    )
  }
}