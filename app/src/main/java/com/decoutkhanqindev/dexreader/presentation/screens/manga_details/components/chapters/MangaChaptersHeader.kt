package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.chapters


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import com.decoutkhanqindev.dexreader.util.LanguageManager
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MangaChaptersHeader(
  selectedLanguage: LanguageValue,
  languageList: ImmutableList<LanguageValue>,
  modifier: Modifier = Modifier,
  onLanguageItemClick: (LanguageValue) -> Unit,
) {
  var isShowLanguageBottomSheet by remember { mutableStateOf(false) }

  if (isShowLanguageBottomSheet) {
    ChapterLanguageListBottomSheet(
      selectedItem = selectedLanguage,
      items = languageList,
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding(),
      onItemClick = onLanguageItemClick,
      onDismissClick = { isShowLanguageBottomSheet = false },
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
      style = MaterialTheme.typography.titleLarge,
    )
    Text(
      text = LanguageManager.labelOf(
        code = selectedLanguage.code,
        flag = selectedLanguage.flag,
      ),
      modifier = Modifier.onClick { isShowLanguageBottomSheet = true },
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.titleMedium,
    )
  }
}

@Preview
@Composable
private fun MangaChaptersHeaderPreview() {
  DexReaderTheme {
    MangaChaptersHeader(
      selectedLanguage = LanguageValue.ENGLISH,
      languageList = persistentListOf(LanguageValue.ENGLISH, LanguageValue.JAPANESE),
      modifier = Modifier.fillMaxWidth(),
      onLanguageItemClick = {}
    )
  }
}
