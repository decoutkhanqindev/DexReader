package com.decoutkhanqindev.dexreader.presentation.ui.common.top_bars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopBar(
  title: String,
  onNavigateBack: () -> Unit,
  isSearchEnabled: Boolean = true,
  onSearchClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  CenterAlignedTopAppBar(
    title = {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.ExtraBold,
      )
    },
    navigationIcon = {
      IconButton(onClick = onNavigateBack) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = stringResource(R.string.back)
        )
      }
    },
    actions = {
      if (isSearchEnabled) {
        IconButton(onClick = onSearchClick) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.search)
          )
        }
      }
    },
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
      containerColor = MaterialTheme.colorScheme.surfaceContainer,
      titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
      navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
      actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ),
    modifier = modifier
  )
}
