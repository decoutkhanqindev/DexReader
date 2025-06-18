package com.decoutkhanqindev.dexreader.presentation.screens.common.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun PersonPicture(modifier: Modifier = Modifier) {
  Image(
    imageVector = Icons.Default.Person,
    contentDescription = null,
    modifier = modifier
      .clip(CircleShape)
      .size(80.dp)
      .background(MaterialTheme.colorScheme.primary)
  )
}