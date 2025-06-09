package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.chapters

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ChapterLanguageItem(
  language: String,
  isSelected: Boolean,
  onSelectedLanguage: () -> Unit,
  modifier: Modifier = Modifier
) {
  DropdownMenuItem(
    text = {
      Text(
        text = language,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal
      )
    },
    onClick = onSelectedLanguage,
    modifier = modifier
  )
}