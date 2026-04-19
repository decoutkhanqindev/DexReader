package com.decoutkhanqindev.dexreader.presentation.screens.reader.components.pages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage
import me.saket.telephoto.zoomable.rememberZoomableImageState

@Composable
fun ChapterPageImage(
  imageUrl: String,
  modifier: Modifier = Modifier,
) {
  val zoomableState = rememberZoomableImageState()

  ZoomableAsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(imageUrl)
      .crossfade(true)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .build(),
    contentDescription = null,
    modifier = modifier,
    state = zoomableState,
    contentScale = ContentScale.Fit,
  )
}

@Preview
@Composable
private fun ChapterPageImagePreview() {
  ChapterPageImage(
    imageUrl = "",
    modifier = Modifier
      .fillMaxWidth()
      .height(400.dp)
  )
}

