package com.decoutkhanqindev.dexreader.presentation.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.ThemeType

@Composable
fun ThemeSelectorList(
  selectedThemeType: ThemeType,
  onSelectedTheme: (ThemeType) -> Unit,
  modifier: Modifier = Modifier
) {
  val themeList = listOf<ThemeItem>(
    ThemeItem(
      type = ThemeType.LIGHT,
      name = stringResource(R.string.light),
      icon = Icons.Default.LightMode
    ),
    ThemeItem(
      type = ThemeType.DARK,
      name = stringResource(R.string.dark),
      icon = Icons.Default.DarkMode
    ),
    ThemeItem(
      type = ThemeType.SYSTEM,
      name = stringResource(R.string.system),
      icon = Icons.Default.PhoneAndroid
    ),
  )

  Card(
    shape = MaterialTheme.shapes.medium,
    elevation = CardDefaults.cardElevation(8.dp),
    modifier = modifier
  ) {
    LazyColumn(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .padding(24.dp)
        .width(165.dp)
    ) {
      item {
        Text(
          text = stringResource(R.string.theme),
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.ExtraBold,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
        )
      }

      items(themeList, key = { it.type }) { theme ->
        ThemeSelectorItem(
          isSelected = selectedThemeType == theme.type,
          theme = theme,
          onSelectedTheme = onSelectedTheme,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  }
}