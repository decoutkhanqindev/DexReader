package com.decoutkhanqindev.dexreader.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel

@Composable
fun FeaturedMangaBanner(
  manga: MangaModel,
  modifier: Modifier = Modifier,
  onItemClick: (String) -> Unit,
) {
  Box(modifier = modifier) {
    AsyncImage(
      model = manga.coverUrl,
      contentDescription = manga.title,
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.Crop
    )

    // Gradient Overlay
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(
              Color.Transparent,
              Color.Black.copy(alpha = 0.5f),
              Color.Black
            )
          )
        )
    )

    Column(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .padding(24.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = stringResource(id = R.string.featured),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primaryContainer,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp
      )
      Text(
        text = manga.title,
        style = MaterialTheme.typography.headlineLarge,
        color = Color.White,
        fontWeight = FontWeight.ExtraBold,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )
      Text(
        text = manga.description,
        style = MaterialTheme.typography.bodyMedium,
        color = Color.White.copy(alpha = 0.7f),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )
      Button(
        onClick = { onItemClick(manga.id) },
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = MaterialTheme.shapes.medium
      ) {
        Text(text = stringResource(id = R.string.read_now), fontWeight = FontWeight.Bold)
      }
    }
  }
}
