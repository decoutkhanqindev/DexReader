package com.decoutkhanqindev.dexreader.presentation.ui.category_details.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R

@Composable
fun ApplyButton(
  onApply: () -> Unit,
  modifier: Modifier = Modifier
) {
  Button(
    shape = MaterialTheme.shapes.medium,
    onClick = onApply,
    modifier = modifier.height(48.dp)
  ) {
    Text(
      text = stringResource(R.string.apply),
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.ExtraBold,
      textAlign = TextAlign.Center
    )
  }
}