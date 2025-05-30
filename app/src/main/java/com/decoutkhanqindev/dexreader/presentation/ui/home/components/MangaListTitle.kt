package com.decoutkhanqindev.dexreader.presentation.ui.home.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun MangaListTitle(
  title: String,
  modifier: Modifier = Modifier
) {
  Text(
    text = title,
    style = MaterialTheme.typography.titleLarge,
    fontWeight = FontWeight.ExtraBold,
    modifier = modifier
  )
}