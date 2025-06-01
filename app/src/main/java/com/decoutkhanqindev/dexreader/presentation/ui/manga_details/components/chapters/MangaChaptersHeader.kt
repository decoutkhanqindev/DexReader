package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.chapters

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.decoutkhanqindev.dexreader.R

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
    Text(
      text = stringResource(R.string.chapters),
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.ExtraBold,
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