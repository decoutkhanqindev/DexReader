package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.MangaCoverArt

@Composable
fun MangaInfoSection(
  manga: MangaModel,
  modifier: Modifier = Modifier,
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

@Preview
@Composable
private fun MangaInfoSectionPreview() {
  MangaInfoSection(
    manga = previewManga,
    modifier = Modifier.fillMaxWidth()
  )
}