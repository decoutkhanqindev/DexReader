package com.decoutkhanqindev.dexreader.presentation.ui.common.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MenuHeader(modifier: Modifier = Modifier) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Image(
      imageVector = Icons.Default.Person,
      contentDescription = null,
      modifier = Modifier
        .weight(0.25f)
        .clip(CircleShape)
        .size(80.dp)
        .background(MaterialTheme.colorScheme.primary)
    )
    Column(
      modifier = Modifier
        .weight(0.75f)
        .fillMaxWidth()
    ) {
      Text(
        text = "Minh Khang",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp)
      )
      Text(
        text = "phamminhkhangy2003@gmail.com",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}