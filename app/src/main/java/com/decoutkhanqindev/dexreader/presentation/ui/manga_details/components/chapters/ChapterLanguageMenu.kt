package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.chapters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R

@Composable
fun ChapterLanguageMenu(
  languageList: List<String>,
  selectedLanguage: String,
  onSelectedLanguage: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var isExpanded by rememberSaveable { mutableStateOf(false) }

  Box(modifier = modifier) {
    Text(
      text = selectedLanguage,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.End,
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { isExpanded = true }
    )
    DropdownMenu(
      expanded = isExpanded,
      onDismissRequest = { isExpanded = false },
      modifier = Modifier.fillMaxWidth(),
    ) {
      if (languageList.isEmpty()) {
        DropdownMenuItem(
          text = {
            Text(
              text = stringResource(R.string.no_languages_available),
              style = MaterialTheme.typography.bodyMedium,
              fontStyle = FontStyle.Italic
            )
          },
          onClick = { isExpanded = false },
          modifier = Modifier.fillMaxWidth()
        )
      } else {
        languageList.forEach { language ->
          ChapterLanguageItem(
            language = language,
            isSelected = language == selectedLanguage,
            onSelectedLanguage = {
              onSelectedLanguage(language)
              isExpanded = false
            },
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    }
  }
}