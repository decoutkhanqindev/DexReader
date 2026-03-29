package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.chapters


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaLanguageValue
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MangaChaptersHeader(
  selectedLanguage: MangaLanguageValue,
  languageList: ImmutableList<MangaLanguageValue>,
  modifier: Modifier = Modifier,
  onLanguageItemClick: (MangaLanguageValue) -> Unit,
) {
  var isShowLanguageBottomSheet by rememberSaveable { mutableStateOf(false) }

  if (isShowLanguageBottomSheet) {
    ChapterLanguageListBottomSheet(
      selectedItem = selectedLanguage,
      items = languageList,
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding(),
      onItemClick = onLanguageItemClick,
      onDismiss = { isShowLanguageBottomSheet = false },
    )
  }

  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = stringResource(R.string.chapters),
      modifier = Modifier
        .weight(0.5f)
        .fillMaxWidth(),
      fontWeight = FontWeight.ExtraBold,
      style = MaterialTheme.typography.titleLarge,
    )
    Text(
      text = stringResource(selectedLanguage.value),
      modifier = Modifier.clickable { isShowLanguageBottomSheet = true },
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.titleMedium,
    )
  }
}
