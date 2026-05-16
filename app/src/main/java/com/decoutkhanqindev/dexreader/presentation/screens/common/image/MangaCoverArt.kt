package com.decoutkhanqindev.dexreader.presentation.screens.common.image

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun MangaCoverArt(
  url: String,
  title: String,
  modifier: Modifier = Modifier,
  onImageLoaded: () -> Unit = {},
) {
  val context = LocalContext.current
  val model = remember(url) {
    ImageRequest.Builder(context)
      .data(url)
      .crossfade(true)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .build()
  }
  val placeholder = painterResource(R.drawable.placeholder)

  AsyncImage(
    model = model,
    contentDescription = title,
    contentScale = ContentScale.FillBounds,
    modifier = modifier.clip(MaterialTheme.shapes.large),
    placeholder = placeholder,
    error = placeholder,
    onSuccess = { onImageLoaded() },
    onError = { onImageLoaded() },
  )
}

@Preview
@Composable
private fun MangaCoverArtPreview() {
  DexReaderTheme {
    MangaCoverArt(
      url = "",
      title = "One Piece",
      modifier = Modifier
        .width(194.dp)
        .height(250.dp),
      onImageLoaded = { }
    )
  }
}
