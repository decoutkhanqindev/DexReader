package com.decoutkhanqindev.dexreader.presentation.ui.reader.components.actions

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
  onNavigatePrevious: () -> Unit,
  onNavigateNext: () -> Unit,
  modifier: Modifier = Modifier
) {
  BottomAppBar(
    actions = {
      IconButton(
        onClick = onNavigatePrevious,
        enabled = canNavigatePrevious,
        modifier = Modifier.weight(0.5f)
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
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = stringResource(R.string.volume_chapter, volume, chapterNumber),
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.ExtraBold,
        )
        Text(
          text = title,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          textAlign = TextAlign.Center,
        )
      }
      IconButton(
        onClick = onNavigateNext,
        enabled = canNavigateNext,
        modifier = Modifier.weight(0.5f)
      ) {
        Icon(
          imageVector = Icons.Default.ChevronRight,
          contentDescription = stringResource(R.string.next_chapter)
        )
      }
    },
    containerColor = MaterialTheme.colorScheme.surfaceContainer,
    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    modifier = modifier
  )
}
