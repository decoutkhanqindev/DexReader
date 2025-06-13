package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.presentation.ui.common.image.MangaCoverArt

@Composable
fun MangaInfoSection(
  manga: Manga,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    MangaCoverArt(
      url = manga.coverUrl,
      title = manga.title,
      modifier = Modifier
        .weight(0.4f)
        .fillMaxWidth()
        .height(222.dp)
    )
    MangaInfo(
      manga = manga,
      modifier = Modifier.weight(0.6f)
    )
  }
}