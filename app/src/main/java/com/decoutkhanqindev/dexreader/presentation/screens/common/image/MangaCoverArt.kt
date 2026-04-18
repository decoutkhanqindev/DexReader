package com.decoutkhanqindev.dexreader.presentation.screens.common.image

import androidx.compose.foundation.Image
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
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.fastShimmer

@Composable
fun MangaCoverArt(
  url: String,
  title: String,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier,
    shape = MaterialTheme.shapes.large,
    elevation = CardDefaults.cardElevation(8.dp),
  ) {
    SubcomposeAsyncImage(
      model = ImageRequest.Builder(LocalContext.current)
        .data(url)
        .crossfade(true)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .build(),
      contentDescription = title,
      contentScale = ContentScale.FillBounds,
      modifier = Modifier.fillMaxSize(),
      loading = {
        Image(
          painter = painterResource(R.drawable.placeholder),
          contentDescription = "$title cover art is loading",
          contentScale = ContentScale.FillHeight,
          modifier = Modifier.fillMaxSize()
        )
      },
      error = {
        Image(
          painter = painterResource(R.drawable.placeholder),
          contentDescription = "$title cover art failed to load",
          contentScale = ContentScale.FillHeight,
          modifier = Modifier.fillMaxSize()
        )
      }
    )
  }
}