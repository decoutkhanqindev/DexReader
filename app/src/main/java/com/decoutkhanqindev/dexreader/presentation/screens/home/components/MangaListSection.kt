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
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaLanguageValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.lists.manga.HorizontalMangaList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


@Composable
fun MangaListSection(
  title: String,
  items: ImmutableList<MangaModel>,
  modifier: Modifier = Modifier,
  onItemClick: (MangaModel) -> Unit,
) {
  Column(modifier = modifier) {
    Text(
      text = title,
      fontWeight = FontWeight.ExtraBold,
      modifier = Modifier.padding(
        start = 16.dp,
        top = 8.dp,
        bottom = 4.dp
      ),
      style = MaterialTheme.typography.titleLarge,
    )
    HorizontalMangaList(
      items = items,
      modifier = Modifier.fillMaxWidth(),
    ) { onItemClick(it) }
  }
}

private val previewMangaSection = persistentListOf(
  MangaModel(
    id = "1",
    title = "One Piece",
    coverUrl = "",
    description = "A pirate adventure.",
    author = "Eiichiro Oda",
    artist = "Eiichiro Oda",
    categories = persistentListOf(CategoryModel(id = "g1", title = "Action")),
    status = MangaStatusValue.ON_GOING,
    contentRating = MangaContentRatingValue.SAFE,
    year = "1997",
    availableLanguages = persistentListOf(MangaLanguageValue.ENGLISH),
    latestChapter = "1110",
    updatedAt = "2024-01-01",
  ),
  MangaModel(
    id = "2",
    title = "Naruto",
    coverUrl = "",
    description = "A ninja story.",
    author = "Masashi Kishimoto",
    artist = "Masashi Kishimoto",
    categories = persistentListOf(CategoryModel(id = "g2", title = "Adventure")),
    status = MangaStatusValue.COMPLETED,
    contentRating = MangaContentRatingValue.SAFE,
    year = "1999",
    availableLanguages = persistentListOf(MangaLanguageValue.ENGLISH),
    latestChapter = "700",
    updatedAt = "2014-11-10",
  ),
  MangaModel(
    id = "3",
    title = "Attack on Titan",
    coverUrl = "",
    description = "Humanity vs Titans.",
    author = "Hajime Isayama",
    artist = "Hajime Isayama",
    categories = persistentListOf(CategoryModel(id = "g3", title = "Action")),
    status = MangaStatusValue.COMPLETED,
    contentRating = MangaContentRatingValue.SAFE,
    year = "2009",
    availableLanguages = persistentListOf(MangaLanguageValue.ENGLISH),
    latestChapter = "139",
    updatedAt = "2021-04-09",
  ),
)

@Preview
@Composable
private fun MangaListSectionPreview() {
  MangaListSection(
    title = "Latest Updates",
    items = previewMangaSection,
    modifier = Modifier.fillMaxWidth(),
    onItemClick = {}
  )
}