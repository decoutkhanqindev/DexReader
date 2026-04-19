package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaLanguageValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MangaInfo(
  manga: MangaModel,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.Start,
  ) {
    Text(
      text = manga.title,
      modifier = Modifier.padding(bottom = 4.dp),
      fontWeight = FontWeight.ExtraBold,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.titleLarge,
    )
    Text(
      text = stringResource(R.string.author, manga.author),
      modifier = Modifier.padding(bottom = 4.dp),
      fontStyle = FontStyle.Italic,
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.bodyLarge,
    )
    Text(
      text = stringResource(R.string.artist, manga.artist),
      modifier = Modifier.padding(bottom = 4.dp),
      fontStyle = FontStyle.Italic,
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.bodyLarge,
    )
    Text(
      text = stringResource(R.string.year, manga.year),
      modifier = Modifier.padding(bottom = 4.dp),
      fontStyle = FontStyle.Italic,
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.bodyLarge,
    )
    Text(
      text = stringResource(R.string.status, stringResource(manga.status.nameRes)),
      modifier = Modifier.padding(bottom = 4.dp),
      fontStyle = FontStyle.Italic,
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.bodyLarge,
    )
    Text(
      text = stringResource(R.string.content_rating, stringResource(manga.contentRating.nameRes)),
      modifier = Modifier.padding(bottom = 4.dp),
      fontStyle = FontStyle.Italic,
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.bodyLarge,
    )
  }
}

internal val previewManga = MangaModel(
  id = "m-001",
  title = "One Piece",
  coverUrl = "",
  description = "Monkey D. Luffy sets off on an adventure to find the legendary treasure known as the One Piece and become the Pirate King.",
  author = "Eiichiro Oda",
  artist = "Eiichiro Oda",
  categories = persistentListOf(
    CategoryModel(id = "g1", title = "Action"),
    CategoryModel(id = "g2", title = "Adventure"),
    CategoryModel(id = "g3", title = "Comedy"),
  ),
  status = MangaStatusValue.ON_GOING,
  contentRating = MangaContentRatingValue.SAFE,
  year = "1997",
  availableLanguages = persistentListOf(MangaLanguageValue.ENGLISH, MangaLanguageValue.JAPANESE),
  latestChapter = "1110",
  updatedAt = "2024-01-01",
)

@Preview
@Composable
private fun MangaInfoPreview() {
  DexReaderTheme {
    MangaInfo(
      manga = previewManga,
      modifier = Modifier
    )
  }
}