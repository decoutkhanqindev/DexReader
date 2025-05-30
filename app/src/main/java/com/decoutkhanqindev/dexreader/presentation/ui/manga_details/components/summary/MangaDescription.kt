package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.summary

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun MangaDescription(
  description: String,
  modifier: Modifier = Modifier
) {
  var isExpanded by rememberSaveable { mutableStateOf(false) }

  Text(
    text = description,
    style = MaterialTheme.typography.bodyMedium,
    fontWeight = FontWeight.Bold,
    maxLines = if (isExpanded) Int.MAX_VALUE else 3,
    overflow = TextOverflow.Ellipsis,
    modifier = modifier.clickable { isExpanded = !isExpanded }
  )
}