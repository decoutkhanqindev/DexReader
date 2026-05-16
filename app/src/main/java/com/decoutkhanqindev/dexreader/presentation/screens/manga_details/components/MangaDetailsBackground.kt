package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
  url: String,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val model = remember(url) {
    ImageRequest.Builder(context)
      .data(url)
      .crossfade(true)
      .size(1080)
      .build()
  }

  AsyncImage(
    model = model,
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
      url = "",
      modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
    )
  }
}