package com.decoutkhanqindev.dexreader.presentation.screens.common.lists.manga

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MangaInfo(
  manga: MangaModel,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = manga.title,
      modifier = Modifier.padding(bottom = 4.dp),
      fontWeight = FontWeight.ExtraBold,
      textAlign = TextAlign.Center,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1,
      style = MaterialTheme.typography.titleMedium,
    )
    Text(
      text = stringResource(R.string.by_author, manga.author),
      modifier = Modifier.padding(bottom = 4.dp),
      fontStyle = FontStyle.Italic,
      fontWeight = FontWeight.Bold,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1,
      style = MaterialTheme.typography.bodyLarge,
    )
    Text(
      text = stringResource(manga.status.nameRes),
      modifier = Modifier.padding(bottom = 4.dp),
      fontStyle = FontStyle.Italic,
      fontWeight = FontWeight.Bold,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1,
      style = MaterialTheme.typography.bodyLarge,
    )
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
  updatedAt = "2024-01-01"
)

@Preview
@Composable
private fun MangaInfoPreview() {
  MangaInfo(
    manga = previewMangaModel,
    modifier = Modifier.fillMaxWidth().height(85.dp)
  )
}
