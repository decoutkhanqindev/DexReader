package com.decoutkhanqindev.dexreader.presentation.screens.reader.components.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R

@Composable
fun NavigateChapterBottomBar(
  volume: String,
  chapterNumber: String,
  title: String,
  canNavigatePrevious: Boolean,
  canNavigateNext: Boolean,
  modifier: Modifier = Modifier,
  onNavigatePrevious: () -> Unit,
  onNavigateNext: () -> Unit,
) {
  BottomAppBar(
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.surfaceContainer,
    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
  ) {
    IconButton(
      onClick = onNavigatePrevious,
      modifier = Modifier.weight(0.5f),
      enabled = canNavigatePrevious,
    ) {
      Icon(
        imageVector = Icons.Default.ChevronLeft,
        contentDescription = stringResource(R.string.pre_chapter)
      )
    }
    Column(
      modifier = Modifier
        .weight(2f)
        .fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(4.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = stringResource(R.string.volume_chapter, volume, chapterNumber),
        fontWeight = FontWeight.ExtraBold,
        style = MaterialTheme.typography.titleLarge,
      )
      if (title.isNotBlank()) {
        Text(
          text = title,
          fontStyle = FontStyle.Italic,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.bodyMedium,
        )
      }
    }
    IconButton(
      onClick = onNavigateNext,
      modifier = Modifier.weight(0.5f),
      enabled = canNavigateNext,
    ) {
      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = stringResource(R.string.next_chapter)
      )
    }
  }
}
