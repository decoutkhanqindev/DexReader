package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.chapters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterLanguageBottomSheet(
  languageList: List<String>,
  selectedLanguage: String,
  onSelectedLanguage: (String) -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val sheetState = rememberModalBottomSheetState()

  ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismiss,
    modifier = modifier
  ) {
    Text(
      text = stringResource(R.string.language_options),
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.ExtraBold,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)
    )
    if (languageList.isEmpty()) {
      Text(
        text = stringResource(R.string.no_languages_available),
        style = MaterialTheme.typography.titleMedium,
        fontStyle = FontStyle.Italic,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp)
      )
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        items(languageList.size, key = { index -> "${languageList[index]}_$index" }) { index ->
          val language = languageList[index]
          val isSelected = language == selectedLanguage
          Text(
            text = language,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Light,
            modifier = Modifier
              .padding(bottom = 8.dp)
              .clickable {
                onSelectedLanguage(language)
                onDismiss()
              }
          )
        }
      }
    }
  }
}