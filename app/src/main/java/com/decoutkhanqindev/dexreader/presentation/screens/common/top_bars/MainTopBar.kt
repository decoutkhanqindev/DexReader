package com.decoutkhanqindev.dexreader.presentation.screens.common.top_bars

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainTopBar(
  title: String,
  isSearchEnabled: Boolean = true,
  modifier: Modifier = Modifier,
  onNavigateToMenuItemScreen: () -> Unit,
  onNavigateToSignInScreen: () -> Unit = {},
) {
  Surface(
    modifier = modifier,
    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
    tonalElevation = 3.dp,
    shadowElevation = 0.dp // We can add a custom shadow if we want
  ) {
    CenterAlignedTopAppBar(
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), // Assuming you have a logo
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = title,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.titleLarge,
            letterSpacing = 1.sp
          )
        }
      },
      navigationIcon = {
        IconButton(onClick = onNavigateToMenuItemScreen) {
          Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = stringResource(R.string.menu),
            tint = MaterialTheme.colorScheme.primary
          )
        }
      },
      actions = {
        if (isSearchEnabled) {
          IconButton(onClick = onNavigateToSignInScreen) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = stringResource(R.string.search),
              tint = MaterialTheme.colorScheme.primary
            )
          }
        }
      },
      colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Color.Transparent,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
      ),
    )
  }
}

@Preview
@Composable
private fun MainTopBarPreview() {
  DexReaderTheme {
    MainTopBar(
      title = "DexReader",
      onNavigateToMenuItemScreen = {}
    )
  }
}

@Preview
@Composable
private fun MainTopBarNoSearchPreview() {
  DexReaderTheme {
    MainTopBar(
      title = "DexReader",
      isSearchEnabled = false,
      onNavigateToMenuItemScreen = {}
    )
  }
}
