package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.chapters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
  var isShowLanguageBottomSheet by rememberSaveable { mutableStateOf(false) }

  if (isShowLanguageBottomSheet) {
    ChapterLanguageBottomSheet(
      languageList = languageList,
      selectedLanguage = selectedLanguage,
      onSelectedLanguage = onSelectedLanguage,
      onDismiss = { isShowLanguageBottomSheet = false },
      modifier = Modifier.fillMaxWidth()
    )
  }

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
    Text(
      text = selectedLanguage,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.clickable { isShowLanguageBottomSheet = true }
    )
  }
}