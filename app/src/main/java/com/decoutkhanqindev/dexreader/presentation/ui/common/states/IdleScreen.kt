package com.decoutkhanqindev.dexreader.presentation.ui.components.common.states

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R

@Composable
fun IdleScreen(
  message: String,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      painter = painterResource(R.drawable.app_icon),
      contentDescription = stringResource(R.string.app_name),
      modifier = Modifier.size(80.dp)
    )
    Text(
      text = message,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Light,
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(start = 8.dp)
    )
  }
}
