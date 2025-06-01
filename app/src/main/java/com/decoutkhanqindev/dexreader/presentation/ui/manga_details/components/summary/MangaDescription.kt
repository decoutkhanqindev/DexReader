package com.decoutkhanqindev.dexreader.presentation.ui.manga_details.components.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.decoutkhanqindev.dexreader.R

@Composable
fun MangaDescription(
  description: String,
  modifier: Modifier = Modifier
) {
  var isExpanded by rememberSaveable { mutableStateOf(false) }

  Column(modifier = modifier) {
    Text(
      text = description,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.Bold,
      maxLines = if (isExpanded) Int.MAX_VALUE else 3,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.fillMaxWidth()
    )
    IconButton(
      onClick = { isExpanded = !isExpanded },
      modifier = Modifier.align(Alignment.End)
    ) {
      Icon(
        imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
        contentDescription = if (isExpanded)
          stringResource(R.string.icon_expand_less)
        else
          stringResource(
            R.string.icon_expand_more
          )
      )
    }
  }
}