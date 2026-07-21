package com.decoutkhanqindev.dexreader.presentation.screens.common.lists.manga

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.animateItemOnAppear
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.MangaCoverArt
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmerLoading
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MangaItem(
  item: MangaModel,
  modifier: Modifier = Modifier,
  onClick: (String) -> Unit,
) {
  val onClick = remember(item.id) { { onClick(item.id) } }
  var isImageLoaded by remember { mutableStateOf(false) }

  Card(
    modifier = modifier
      .animateItemOnAppear()
      .onClick(shape = MaterialTheme.shapes.medium) { onClick() },
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    shape = MaterialTheme.shapes.medium,
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    )
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .shimmerLoading(isEnable = !isImageLoaded)
    ) {
      MangaCoverArt(
        url = item.coverUrl,
        title = item.title,
        modifier = Modifier.fillMaxSize(),
        onImageLoaded = { isImageLoaded = true }
      )

      // Modern Gradient Overlay
      Box(
        modifier = Modifier
          .fillMaxSize()
          .blurBackground(
            color = Color.Black,
            topAlpha = 0f,
            topCenterAlpha = 0.1f,
            bottomCenterAlpha = 0.8f,
            bottomAlpha = 1f,
            startY = 350f,
          )
      )

      // Status Badge
      Surface(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(8.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
        shape = MaterialTheme.shapes.small,
        tonalElevation = 4.dp
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = item.status.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(12.dp)
          )
          Text(
            text = stringResource(item.status.nameRes).uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
        }
      }

      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start,
      ) {
        MangaInfo(
          manga = item,
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
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
        updatedAt = "2024-01-01",
        rating = "8.5",
        follows = "2.3M",
      ), modifier = Modifier
        .width(194.dp)
        .height(250.dp), onClick = {})
  }
}