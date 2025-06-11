package com.decoutkhanqindev.dexreader.presentation.ui.common.lists.manga

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.Manga

@Composable
fun MangaItem(
  manga: Manga,
  onSelectedManga: (Manga) -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier,
    onClick = { onSelectedManga(manga) },
    elevation = CardDefaults.cardElevation(8.dp),
    shape = MaterialTheme.shapes.large,
  ) {
    Box(modifier = Modifier.fillMaxWidth()) {
      AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
          .data(manga.coverUrl)
          .crossfade(true)
          .memoryCachePolicy(CachePolicy.ENABLED)
          .diskCachePolicy(CachePolicy.ENABLED)
          .build(),
        contentDescription = manga.title,
        error = painterResource(R.drawable.placeholder),
        placeholder = painterResource(R.drawable.placeholder),
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
      )
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        MangaInfo(
          manga = manga,
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