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
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaLanguageValue
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterLanguageBottomSheet(
  languageList: ImmutableList<MangaLanguageValue>,
  selectedLanguage: MangaLanguageValue,
  modifier: Modifier = Modifier,
  onSelectedLanguage: (MangaLanguageValue) -> Unit,
  onDismiss: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState()

  ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismiss,
    modifier = modifier
  ) {
    Text(
      text = stringResource(R.string.language_options),
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp),
      fontWeight = FontWeight.ExtraBold,
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleLarge,
    )
    if (languageList.isEmpty()) {
      Text(
        text = stringResource(R.string.no_languages_available),
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp),
        fontStyle = FontStyle.Italic,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
      )
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        items(
          count = languageList.size,
          key = { index -> "${languageList[index].name}_$index" }
        ) { index ->
          val language = languageList[index]
          val isSelected = language == selectedLanguage
          Text(
            text = stringResource(language.value),
            modifier = Modifier
              .padding(bottom = 8.dp)
              .clickable {
                onSelectedLanguage(language)
                onDismiss()
              },
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Light,
            style = MaterialTheme.typography.titleMedium,
          )
        }
      }
    }
  }
}
