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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaLanguageValue
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterLanguageListBottomSheet(
  selectedItem: MangaLanguageValue,
  items: ImmutableList<MangaLanguageValue>,
  modifier: Modifier = Modifier,
  onItemClick: (MangaLanguageValue) -> Unit,
  onDismissClick: () -> Unit,
) {
  ModalBottomSheet(
    onDismissRequest = onDismissClick,
    modifier = modifier,
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
    if (items.isEmpty()) {
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
          count = items.size,
          key = { index -> "${items[index].name}_$index" }
        ) { index ->
          val language = items[index]
          val isSelected = language == selectedItem
          Text(
            text = stringResource(language.value),
            modifier = Modifier
              .padding(bottom = 8.dp)
              .clickable {
                onItemClick(language)
                onDismissClick()
              },
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Light,
            style = MaterialTheme.typography.titleMedium,
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ChapterLanguageListBottomSheetPreview() {
  DexReaderTheme {
    ChapterLanguageListBottomSheet(
      selectedItem = MangaLanguageValue.ENGLISH,
      items = persistentListOf(
        MangaLanguageValue.ENGLISH,
        MangaLanguageValue.JAPANESE,
        MangaLanguageValue.FRENCH,
        MangaLanguageValue.SPANISH,
      ),
      onItemClick = {},
      onDismissClick = {}
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ChapterLanguageListBottomSheetEmptyPreview() {
  DexReaderTheme {
    ChapterLanguageListBottomSheet(
      selectedItem = MangaLanguageValue.ENGLISH,
      items = persistentListOf(),
      onItemClick = {},
      onDismissClick = {}
    )
  }
}
