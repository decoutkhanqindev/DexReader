package com.decoutkhanqindev.dexreader.presentation.screens.onboarding.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick

@Composable
fun OnboardingActions(
  isLastPage: Boolean,
  modifier: Modifier = Modifier,
  onSkipClick: () -> Unit,
  onNextClick: () -> Unit,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (!isLastPage) {
      Text(
        text = stringResource(R.string.skip),
        modifier = Modifier
          .onClick(shape = MaterialTheme.shapes.large) { onSkipClick() }
          .padding(horizontal = 24.dp, vertical = 16.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontStyle = FontStyle.Italic,
        style = MaterialTheme.typography.titleMedium,
      )
    }

    ActionButton(
      isHighlighted = true,
      modifier = Modifier.then(if (isLastPage) Modifier.fillMaxWidth() else Modifier),
      backgroundColor = MaterialTheme.colorScheme.primary,
      onClick = onNextClick
    ) {
      Text(
        text = stringResource(if (isLastPage) R.string.get_started else R.string.next),
        color = MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.ExtraBold,
        style = MaterialTheme.typography.titleMedium,
      )

      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
          .size(24.dp)
          .padding(start = 8.dp)
      )
    }
  }
}
