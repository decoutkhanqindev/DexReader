package com.decoutkhanqindev.dexreader.presentation.screens.common.top_bars

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopBar(
  title: String,
  isSearchEnabled: Boolean = true,
  modifier: Modifier = Modifier,
  onNavigateBack: () -> Unit,
  onNavigateToSearchScreen: () -> Unit = {},
) {
  CenterAlignedTopAppBar(
    title = {
      Text(
        text = title,
        fontWeight = FontWeight.ExtraBold,
        style = MaterialTheme.typography.titleLarge,
      )
    },
    modifier = modifier,
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
        IconButton(onClick = onNavigateToSearchScreen) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.search)
          )
        }
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surfaceContainer,
      scrolledContainerColor = Color.Unspecified,
      navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
      titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
      actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ),
  )
}

@Preview
@Composable
private fun DetailsTopBarPreview() {
  DexReaderTheme {
    DetailsTopBar(
      title = "One Piece",
      onNavigateBack = {}
    )
  }
}

@Preview
@Composable
private fun DetailsTopBarNoSearchPreview() {
  DexReaderTheme {
    DetailsTopBar(
      title = "One Piece",
      isSearchEnabled = false,
      onNavigateBack = {}
    )
  }
}
