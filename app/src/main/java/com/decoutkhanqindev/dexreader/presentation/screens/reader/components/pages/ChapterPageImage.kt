package com.decoutkhanqindev.dexreader.presentation.screens.reader.components.pages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage
import me.saket.telephoto.zoomable.rememberZoomableImageState

@Composable
fun ChapterPageImage(
  url: String,
  modifier: Modifier = Modifier,
) {
  val zoomableState = rememberZoomableImageState()
  val context = LocalContext.current
  val imageRequest = remember(url) {
    ImageRequest.Builder(context)
      .data(url)
      .crossfade(true)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .build()
  }

  ZoomableAsyncImage(
    model = imageRequest,
    contentDescription = null,
    modifier = modifier,
    state = zoomableState,
    contentScale = ContentScale.Fit,
  )
}

@Preview
@Composable
private fun ChapterPageImagePreview() {
  DexReaderTheme {
    ChapterPageImage(
      url = "",
      modifier = Modifier
        .fillMaxWidth()
        .height(400.dp)
    )
  }
}

