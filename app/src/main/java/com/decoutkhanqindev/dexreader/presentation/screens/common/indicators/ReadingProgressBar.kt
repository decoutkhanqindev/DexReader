package com.decoutkhanqindev.dexreader.presentation.screens.common.indicators

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun ReadingProgressBar(
  lastReadPage: Int,
  pageCount: Int,
  modifier: Modifier = Modifier,
  progressColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
  trackColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
) {
  val progressFloat = remember(lastReadPage, pageCount) {
    if (pageCount > 0) (lastReadPage.toFloat() / pageCount.toFloat()).coerceIn(0f, 1f) else 0f
  }
  val animatedProgress by animateFloatAsState(
    targetValue = progressFloat,
    animationSpec = tween(durationMillis = 500),
    label = "readingProgress"
  )
  val progressInt by remember {
    derivedStateOf { (animatedProgress * 100).toInt() }
  }

  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(6.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = stringResource(R.string.reader_title, lastReadPage, pageCount),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      Text(
        text = "${progressInt}%",
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Black,
        color = progressColor,
      )
    }

    LinearProgressIndicator(
      progress = { animatedProgress },
      modifier = Modifier.fillMaxWidth(),
      color = progressColor,
      trackColor = trackColor,
      strokeCap = StrokeCap.Round,
      gapSize = 0.dp,
      drawStopIndicator = {},
    )
  }
}

@Preview
@Composable
private fun ReadingProgressBarPreview() {
  DexReaderTheme {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
      ReadingProgressBar(
        lastReadPage = 12,
        pageCount = 46,
        modifier = Modifier.fillMaxWidth()
      )
      ReadingProgressBar(
        lastReadPage = 40,
        pageCount = 46,
        modifier = Modifier.fillMaxWidth()
      )
      ReadingProgressBar(
        lastReadPage = 46,
        pageCount = 46,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}
