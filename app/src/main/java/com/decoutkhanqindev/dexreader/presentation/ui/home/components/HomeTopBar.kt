package com.decoutkhanqindev.dexreader.presentation.ui.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.decoutkhanqindev.dexreader.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeTopBar(
  title: String,
  onMenuClick: () -> Unit,
  onSearchClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  CenterAlignedTopAppBar(
    title = {
      Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.ExtraBold,
      )
    },
    navigationIcon = {
      IconButton(onClick = onMenuClick) {
        Icon(
          imageVector = Icons.Default.Menu, contentDescription = stringResource(R.string.menu)
        )
      }
    },
    actions = {
      IconButton(onClick = onSearchClick) {
        Icon(
          imageVector = Icons.Default.Search, contentDescription = stringResource(R.string.search)
        )
      }
    },
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer,
      titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
      navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
      actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ),
    modifier = modifier
  )
}
