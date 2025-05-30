package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.summary

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.decoutkhanqindev.dexreader.R

@Composable
fun MangaSummaryTitle(modifier: Modifier = Modifier) {
  Text(
    text = stringResource(R.string.summary),
    style = MaterialTheme.typography.titleLarge,
    fontWeight = FontWeight.ExtraBold,
    modifier = modifier
  )
}