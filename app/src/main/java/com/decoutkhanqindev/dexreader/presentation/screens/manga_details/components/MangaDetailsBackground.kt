package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun MangaDetailsBackground(
  imageUrl: String,
  modifier: Modifier = Modifier,
) {
  AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(imageUrl)
      .crossfade(true)
      .size(1080)
      .build(),
    contentDescription = null,
    modifier = modifier,
    contentScale = ContentScale.Crop,
  )
}

@Preview
@Composable
private fun MangaDetailsBackgroundPreview() {
  DexReaderTheme {
    MangaDetailsBackground(
      imageUrl = "",
      modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
    )
  }
}