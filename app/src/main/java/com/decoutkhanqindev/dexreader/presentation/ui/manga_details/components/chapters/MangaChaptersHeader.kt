package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.chapters

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MangaChaptersHeader(
  selectedLanguage: String,
  languageList: List<String>,
  onSelectedLanguage: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    MangaChaptersTitle(
      modifier = Modifier
        .weight(0.5f)
        .fillMaxWidth()
    )
    ChapterLanguageMenu(
      languageList = languageList,
      selectedLanguage = selectedLanguage,
      onSelectedLanguage = onSelectedLanguage,
      modifier = Modifier
        .weight(0.5f)
        .fillMaxWidth()
    )
  }
}