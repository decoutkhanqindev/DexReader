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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HorizontalMangaList(
  items: ImmutableList<MangaModel>,
  modifier: Modifier = Modifier,
  onItemClick: (MangaModel) -> Unit,
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
          modifier = Modifier
            .padding(4.dp)
            .width(194.dp)
            .height(250.dp),
        ) { onItemClick(it) }
      }
    }
  }
}

@Preview
@Composable
private fun HorizontalMangaListPreview() {
  DexReaderTheme {
    HorizontalMangaList(
      items = persistentListOf(
        MangaModel(
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
        MangaModel(
          id = "manga-002",
          title = "Naruto",
          coverUrl = "",
          description = "Follow Naruto Uzumaki on his journey to become Hokage.",
          author = "Masashi Kishimoto",
          artist = "Masashi Kishimoto",
          categories = persistentListOf(),
          status = MangaStatusValue.COMPLETED,
          contentRating = MangaContentRatingValue.SAFE,
          year = "1999",
          availableLanguages = persistentListOf(),
          latestChapter = "700",
          updatedAt = "2014-11-10"
        )
      ),
      modifier = Modifier
        .fillMaxWidth()
        .height(270.dp),
      onItemClick = {}
    )
  }
}
