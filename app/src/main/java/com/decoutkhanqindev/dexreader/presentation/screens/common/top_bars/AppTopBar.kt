package com.decoutkhanqindev.dexreader.presentation.screens.common.top_bars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.screens.common.indicators.ReadingProgressBar
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
  modifier: Modifier = Modifier,
  centerIcon: ImageVector? = null,
  centerTitle: String? = null,
  centerContent: (@Composable () -> Unit)? = null,
  onCenterClick: () -> Unit = {},
  leftIcon: ImageVector? = null,
  leftTitle: String? = null,
  leftContent: (@Composable () -> Unit)? = null,
  onLeftClick: () -> Unit = {},
  rightIcon: ImageVector? = null,
  rightTitle: String? = null,
  rightContent: (@Composable () -> Unit)? = null,
  onRightClick: () -> Unit = {},
  containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
  centerContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
  leftContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
  rightContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
  Surface(
    modifier = Modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
    tonalElevation = 3.dp,
    shadowElevation = 0.dp
  ) {
    CenterAlignedTopAppBar(
      title = {
        when {
          centerContent != null -> centerContent()

          centerIcon != null -> Box(
            modifier = Modifier.onClick(
              shape = CircleShape,
              action = onCenterClick
            )
          ) {
            Icon(
              modifier = Modifier.padding(8.dp),
              imageVector = centerIcon,
              contentDescription = null
            )
          }

          centerTitle != null -> Box(
            modifier = Modifier.onClick { onCenterClick() }
          ) {
            Text(
              modifier = Modifier.padding(8.dp),
              text = centerTitle,
              fontWeight = FontWeight.ExtraBold,
              style = MaterialTheme.typography.titleLarge,
            )
          }
        }
      },
      modifier = modifier,
      navigationIcon = {
        when {
          leftContent != null -> leftContent()

          leftIcon != null -> Box(
            modifier = Modifier.onClick(
              shape = CircleShape,
              action = onLeftClick
            )
          ) {
            Icon(
              modifier = Modifier.padding(8.dp),
              imageVector = leftIcon,
              contentDescription = null
            )
          }

          leftTitle != null -> Box(
            modifier = Modifier.onClick { onLeftClick() }
          ) {
            Text(
              modifier = Modifier.padding(8.dp),
              text = leftTitle,
              fontWeight = FontWeight.ExtraBold,
              style = MaterialTheme.typography.bodyMedium,
            )
          }
        }
      },
      actions = {
        when {
          rightContent != null -> rightContent()

          rightIcon != null ->
            Box(
              modifier = Modifier.onClick(
                shape = CircleShape,
                action = onRightClick
              )
            ) {
              Icon(
                modifier = Modifier.padding(8.dp),
                imageVector = rightIcon,
                contentDescription = null
              )
            }

          rightTitle != null -> Box(
            modifier = Modifier.onClick { onRightClick() }
          ) {
            Text(
              modifier = Modifier.padding(8.dp),
              text = rightTitle,
              fontWeight = FontWeight.ExtraBold,
              style = MaterialTheme.typography.bodyMedium,
            )
          }
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor = containerColor,
        titleContentColor = centerContentColor,
        navigationIconContentColor = leftContentColor,
        actionIconContentColor = rightContentColor,
      ),
    )
  }
}

@Preview
@Composable
private fun AppTopBarMainPreview() {
  DexReaderTheme {
    Surface(
      modifier = Modifier.fillMaxWidth(),
      color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
      tonalElevation = 3.dp,
      shadowElevation = 0.dp
    ) {
      AppTopBar(
        leftIcon = Icons.Default.Menu,
        centerTitle = "DexReader",
        rightIcon = Icons.Default.Search,
        containerColor = Color.Transparent,
        centerContentColor = MaterialTheme.colorScheme.onSurface,
        leftContentColor = MaterialTheme.colorScheme.primary,
        rightContentColor = MaterialTheme.colorScheme.primary,
      )
    }
  }
}

@Preview
@Composable
private fun AppTopBarMainNoSearchPreview() {
  DexReaderTheme {
    Surface(
      modifier = Modifier.fillMaxWidth(),
      color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
      tonalElevation = 3.dp,
      shadowElevation = 0.dp
    ) {
      AppTopBar(
        leftIcon = Icons.Default.Menu,
        centerTitle = "DexReader",
        rightIcon = null,
        containerColor = Color.Transparent,
        centerContentColor = MaterialTheme.colorScheme.onSurface,
        leftContentColor = MaterialTheme.colorScheme.primary,
        rightContentColor = MaterialTheme.colorScheme.primary,
      )
    }
  }
}

@Preview
@Composable
private fun AppTopBarDetailsPreview() {
  DexReaderTheme {
    AppTopBar(
      leftIcon = Icons.AutoMirrored.Filled.ArrowBack,
      centerTitle = "One Piece",
      rightIcon = Icons.Default.Search,
    )
  }
}

@Preview
@Composable
private fun AppTopBarDetailsNoSearchPreview() {
  DexReaderTheme {
    AppTopBar(
      leftIcon = Icons.AutoMirrored.Filled.ArrowBack,
      centerTitle = "One Piece",
      rightIcon = null,
    )
  }
}

@Preview
@Composable
private fun AppTopBarDetailsWithProgressPreview() {
  DexReaderTheme {
    AppTopBar(
      leftIcon = Icons.AutoMirrored.Filled.ArrowBack,
      centerContent = {
        ReadingProgressBar(
          lastReadPage = 12,
          pageCount = 46,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        )
      },
    )
  }
}
