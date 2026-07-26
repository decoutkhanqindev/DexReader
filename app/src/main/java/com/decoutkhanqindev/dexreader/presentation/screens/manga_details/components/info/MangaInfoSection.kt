package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaLanguageValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.badges.MangaRatingChip
import com.decoutkhanqindev.dexreader.presentation.screens.common.badges.MangaStatusBadge
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.MangaCoverArt
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MangaInfoSection(
  manga: MangaModel,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    MangaCoverArt(
      url = manga.coverUrl,
      title = manga.title,
      modifier = Modifier
        .width(180.dp)
        .height(260.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
      text = manga.title,
      style = MaterialTheme.typography.titleLarge,
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = androidx.compose.ui.text.style.TextAlign.Center,
      modifier = Modifier.padding(horizontal = 16.dp)
    )

    Text(
      text = manga.author,
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(top = 4.dp)
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
      modifier = Modifier.fillMaxWidth()
    ) {
      InfoChip(label = manga.year)
      Spacer(modifier = Modifier.width(12.dp))
      MangaStatusBadge(status = manga.status)
      Spacer(modifier = Modifier.width(12.dp))
      MangaRatingChip(rating = manga.rating)
    }
  }
}

@Composable
private fun InfoChip(label: String) {
  androidx.compose.material3.Surface(
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    shape = MaterialTheme.shapes.small,
    tonalElevation = 4.dp,
    shadowElevation = 4.dp
  ) {
    Text(
      text = label.uppercase(),
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
      style = MaterialTheme.typography.labelSmall,
      fontWeight = FontWeight.Black,
      color = MaterialTheme.colorScheme.onSurfaceVariant
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
  rating = "9.1",
  follows = "2.3M",
)

@Preview
@Composable
private fun MangaInfoSectionPreview() {
  DexReaderTheme {
    MangaInfoSection(
      manga = previewManga,
      modifier = Modifier.fillMaxWidth()
    )
  }
}
