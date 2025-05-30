package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.chapters

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.decoutkhanqindev.dexreader.R


@Composable
fun NoChaptersAvailableMessage(modifier: Modifier = Modifier) {
  Text(
    text = stringResource(R.string.no_chapters_available),
    style = MaterialTheme.typography.titleMedium,
    fontWeight = FontWeight.Bold,
    fontStyle = FontStyle.Italic,
    textAlign = TextAlign.Center,
    modifier = modifier
  )
}