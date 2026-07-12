package com.decoutkhanqindev.dexreader.presentation.screens.common.lists.manga


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
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
fun VerticalGridMangaList(
  gridState: (() -> LazyGridState),
  items: ImmutableList<MangaModel>,
  modifier: Modifier = Modifier,
  onItemClick: (String) -> Unit,
  loadMoreContent: @Composable () -> Unit,
) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    modifier = modifier,
    state = gridState(),
    verticalArrangement = Arrangement.spacedBy(2.dp),
    horizontalArrangement = Arrangement.spacedBy(2.dp),
  ) {
    items(items = items, key = MangaModel::id) {
      MangaItem(
        item = it,
        modifier = Modifier
          .padding(4.dp)
          .fillMaxWidth()
          .height(250.dp),
        onClick = onItemClick
      )
    }
    item(span = { GridItemSpan(maxLineSpan) }) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp)
      ) { loadMoreContent() }
    }
  }
}

@Preview
@Composable
private fun VerticalGridMangaListPreview() {
  DexReaderTheme {
    val lazyGridState = rememberLazyGridState()

    VerticalGridMangaList(
      gridState = { lazyGridState },
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
          updatedAt = "2024-01-01",
          rating = "9.1",
          follows = "2.3M",
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
          updatedAt = "2014-11-10",
          rating = "8.7",
          follows = "1.8M",
        ),
        MangaModel(
          id = "manga-003",
          title = "Attack on Titan",
          coverUrl = "",
          description = "Humanity fights for survival against giant humanoid creatures.",
          author = "Hajime Isayama",
          artist = "Hajime Isayama",
          categories = persistentListOf(),
          status = MangaStatusValue.COMPLETED,
          contentRating = MangaContentRatingValue.SAFE,
          year = "2009",
          availableLanguages = persistentListOf(),
          latestChapter = "139",
          updatedAt = "2021-04-09",
          rating = "9.0",
          follows = "1.2M",
        ),
        MangaModel(
          id = "manga-004",
          title = "Demon Slayer",
          coverUrl = "",
          description = "Tanjiro joins the Demon Slayer Corps to avenge his family.",
          author = "Koyoharu Gotouge",
          artist = "Koyoharu Gotouge",
          categories = persistentListOf(),
          status = MangaStatusValue.COMPLETED,
          contentRating = MangaContentRatingValue.SAFE,
          year = "2016",
          availableLanguages = persistentListOf(),
          latestChapter = "205",
          updatedAt = "2020-05-18",
          rating = "8.9",
          follows = "980.5K",
        )
      ),
      modifier = Modifier.fillMaxSize(),
      onItemClick = {},
      loadMoreContent = {}
    )
  }
}