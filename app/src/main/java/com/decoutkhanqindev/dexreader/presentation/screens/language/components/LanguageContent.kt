package com.decoutkhanqindev.dexreader.presentation.screens.language.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageTypeValue
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import com.decoutkhanqindev.dexreader.util.LanguageManager
import kotlinx.collections.immutable.persistentListOf

@Composable
fun LanguageContent(
  type: LanguageTypeValue,
  selectedLanguage: LanguageValue?,
  appliedLanguage: LanguageValue,
  modifier: Modifier = Modifier,
  onLanguageClick: (LanguageValue) -> Unit,
  onDoneClick: () -> Unit,
) {
  val displayLanguage = LanguageManager.current
  val deviceLanguageCode = LanguageManager.deviceLanguageCode()
  val displayNames = remember(displayLanguage) {
    LanguageValue.displayNamesFor(displayIn = displayLanguage)
  }
  val languages = remember(displayLanguage, deviceLanguageCode) {
    LanguageValue.sortedForDisplay(
      deviceLanguageCode = deviceLanguageCode,
      displayNames = displayNames,
    )
  }
  val isDoneEnabled = when (type) {
    LanguageTypeValue.SELECTION -> selectedLanguage != null
    LanguageTypeValue.SETTING -> selectedLanguage != null && selectedLanguage != appliedLanguage
  }

  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(
        start = 16.dp,
        top = 8.dp,
        end = 16.dp,
        bottom = 96.dp,
      ),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(languages, key = LanguageValue::name) { language ->
        val isSelected = when (type) {
          LanguageTypeValue.SELECTION -> language == selectedLanguage
          LanguageTypeValue.SETTING -> language == (selectedLanguage ?: appliedLanguage)
        }
        val onClick = remember(language) { { onLanguageClick(language) } }

        LanguageItem(
          language = language,
          displayName = displayNames.getValue(language),
          isSelected = isSelected,
          modifier = Modifier.fillMaxWidth(),
          onClick = onClick,
        )
      }
    }

    ActionButton(
      isEnabled = isDoneEnabled,
      isHighlighted = isDoneEnabled,
      backgroundColor = MaterialTheme.colorScheme.primary,
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .blurBackground(alphas = persistentListOf(0f, 0f, 1f, 1f))
        .navigationBarsPadding()
        .padding(16.dp),
      onClick = onDoneClick
    ) {
      Text(
        text = stringResource(R.string.done),
        color = MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.ExtraBold,
        style = MaterialTheme.typography.titleMedium,
      )

      Icon(
        imageVector = Icons.Filled.Done,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
          .size(24.dp)
          .padding(start = 8.dp)
      )
    }
  }
}

@Preview
@Composable
private fun LanguageContentPreview() {
  DexReaderTheme {
    LanguageContent(
      type = LanguageTypeValue.SETTING,
      selectedLanguage = LanguageValue.VIETNAMESE,
      appliedLanguage = LanguageValue.ENGLISH,
      modifier = Modifier.fillMaxSize(),
      onLanguageClick = {},
      onDoneClick = {},
    )
  }
}

@Preview
@Composable
private fun LanguageContentNothingSelectedPreview() {
  DexReaderTheme {
    LanguageContent(
      type = LanguageTypeValue.SELECTION,
      selectedLanguage = null,
      appliedLanguage = LanguageValue.ENGLISH,
      modifier = Modifier.fillMaxSize(),
      onLanguageClick = {},
      onDoneClick = {},
    )
  }
}
