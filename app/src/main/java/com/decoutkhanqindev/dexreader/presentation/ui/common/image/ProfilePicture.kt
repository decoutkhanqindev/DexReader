package com.decoutkhanqindev.dexreader.presentation.ui.common.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun ProfilePicture(
  url: String,
  name: String,
  modifier: Modifier = Modifier
) {
  AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(url)
      .crossfade(true)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .build(),
    contentDescription = name,
    contentScale = ContentScale.Crop,
    modifier = modifier
      .clip(CircleShape)
      .size(80.dp)
      .background(MaterialTheme.colorScheme.primary)
  )
}