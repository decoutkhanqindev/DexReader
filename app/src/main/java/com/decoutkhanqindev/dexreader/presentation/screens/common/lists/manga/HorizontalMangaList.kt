package com.decoutkhanqindev.dexreader.presentation.screens.common.lists.manga


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun HorizontalMangaList(
  items: ImmutableList<MangaModel>,
  onItemClick: (MangaModel) -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(modifier = modifier) {
    LazyRow(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(2.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      items(
        items = items,
        key = MangaModel::id
      ) { manga ->
        MangaItem(
          item = manga,
          onClick = onItemClick,
          modifier = Modifier
            .padding(4.dp)
            .width(194.dp)
            .height(250.dp)
        )
      }
    }
  }
}
