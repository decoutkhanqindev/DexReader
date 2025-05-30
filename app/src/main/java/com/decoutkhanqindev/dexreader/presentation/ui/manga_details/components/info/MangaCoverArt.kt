package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.info

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
fun MangaCoverArt(
  manga: Manga,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = MaterialTheme.shapes.large,
    elevation = CardDefaults.cardElevation(8.dp),
  ) {
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
  }
}