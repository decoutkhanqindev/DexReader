package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.chapters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.locals.LocalLanguageManager
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterLanguageListBottomSheet(
  selectedItem: LanguageValue,
  items: ImmutableList<LanguageValue>,
  modifier: Modifier = Modifier,
  onItemClick: (LanguageValue) -> Unit,
  onDismissClick: () -> Unit,
) {
  val languageManager = LocalLanguageManager.current
  val displayLanguage = LanguageValue.fromCode(LocalConfiguration.current.locales[0].toLanguageTag())

  ModalBottomSheet(
    onDismissRequest = onDismissClick,
    modifier = modifier,
  ) {
    Text(
      text = stringResource(R.string.language_options),
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp),
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
        items(items, key = LanguageValue::name) {
          val isSelected = it == selectedItem
          val onClick = remember(it) { { onItemClick(it) } }

          Text(
            text = it.labelFor(displayIn = displayLanguage, languageManager = languageManager),
            modifier = Modifier
              .padding(bottom = 8.dp)
              .onClick {
                onClick()
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
