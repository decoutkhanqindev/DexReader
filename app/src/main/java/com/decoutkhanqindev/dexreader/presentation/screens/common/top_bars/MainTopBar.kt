package com.decoutkhanqindev.dexreader.presentation.screens.common.top_bars

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainTopBar(
  title: String,
  isSearchEnabled: Boolean = true,
  modifier: Modifier = Modifier,
  onNavigateToMenuItemScreen: () -> Unit,
  onNavigateToSignInScreen: () -> Unit = {},
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
      IconButton(onClick = onNavigateToMenuItemScreen) {
        Icon(
          imageVector = Icons.Default.Menu,
          contentDescription = stringResource(R.string.menu)
        )
      }
    },
    actions = {
      if (isSearchEnabled) {
        IconButton(onClick = onNavigateToSignInScreen) {
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
private fun MainTopBarPreview() {
  MainTopBar(
    title = "DexReader",
    onNavigateToMenuItemScreen = {}
  )
}

@Preview
@Composable
private fun MainTopBarNoSearchPreview() {
  MainTopBar(
    title = "DexReader",
    isSearchEnabled = false,
    onNavigateToMenuItemScreen = {}
  )
}
