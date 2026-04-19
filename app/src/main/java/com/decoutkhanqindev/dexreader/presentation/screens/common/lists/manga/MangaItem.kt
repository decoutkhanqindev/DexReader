package com.decoutkhanqindev.dexreader.presentation.screens.common.lists.manga

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.MangaCoverArt
import com.decoutkhanqindev.dexreader.presentation.screens.common.onScalableClick
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MangaItem(
  item: MangaModel,
  modifier: Modifier = Modifier,
  onClick: (MangaModel) -> Unit,
) {
  Card(
    modifier = modifier.onScalableClick(shape = CardDefaults.shape) { onClick(item) },
    elevation = CardDefaults.cardElevation(8.dp),
    shape = MaterialTheme.shapes.large,
  ) {
    Box(modifier = Modifier.fillMaxWidth()) {
      MangaCoverArt(
        url = item.coverUrl,
        title = item.title,
        modifier = Modifier.fillMaxSize()
      )
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        MangaInfo(
          manga = item,
          modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .background(MaterialTheme.colorScheme.surface.copy(0.8f))
            .padding(horizontal = 4.dp)
        )
      }
    }
  }
}

@Preview
@Composable
private fun MangaItemPreview() {
  DexReaderTheme {
    MangaItem(
      item = MangaModel(
        id = "manga-001",
        title = "One Piece",
        coverUrl = "",
        description = "Follow Monkey D. Luffy on his grand adventure to become King of the Pirates.",
        author = "Eiichiro Oda",
        artist = "Eiichiro Oda",
        categories = persistentListOf(),
        status = MangaStatusValue.ON_GOING,
        contentRating = MangaContentRatingValue.SAFE,
        year = "1997",
        availableLanguages = persistentListOf(),
        latestChapter = "1100",
        updatedAt = "2024-01-01"
      ),
      modifier = Modifier
        .width(194.dp)
        .height(250.dp),
      onClick = {}
    )
  }
}