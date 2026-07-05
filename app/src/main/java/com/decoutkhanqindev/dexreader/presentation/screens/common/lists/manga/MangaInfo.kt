package com.decoutkhanqindev.dexreader.presentation.screens.common.lists.manga

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
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
    verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
    horizontalAlignment = Alignment.Start,
  ) {
    Text(
      text = manga.title,
      color = Color.White,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Start,
      overflow = TextOverflow.Ellipsis,
      maxLines = 2,
      style = MaterialTheme.typography.titleMedium,
    )
    Text(
      text = manga.author,
      color = Color.White.copy(alpha = 0.8f),
      fontWeight = FontWeight.Medium,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1,
      style = MaterialTheme.typography.labelMedium,
    )
    if (manga.rating.isNotEmpty()) {
      Text(
        text = stringResource(R.string.rating_compact, manga.rating),
        color = Color.White.copy(alpha = 0.8f),
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        style = MaterialTheme.typography.labelSmall,
      )
    }
  }
}

private val previewMangaModel = MangaModel(
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
  rating = "8.5",
  follows = "2.3M",
)

@Preview
@Composable
private fun MangaInfoPreview() {
  DexReaderTheme {
    MangaInfo(
      manga = previewMangaModel,
      modifier = Modifier
        .fillMaxWidth()
        .height(85.dp)
    )
  }
}
