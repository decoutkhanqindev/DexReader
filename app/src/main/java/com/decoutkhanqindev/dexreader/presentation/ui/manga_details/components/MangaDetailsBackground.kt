package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun MangaDetailsBackground(
  imageUrl: String,
  contentDesc: String,
  modifier: Modifier = Modifier
) {
  AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(imageUrl)
      .crossfade(true)
      .size(1080)
      .build(),
    contentDescription = contentDesc,
    contentScale = ContentScale.Crop,
    modifier = modifier
  )
}