package com.decoutkhanqindev.dexreader.presentation.screens.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.value.onboarding.OnboardingPageValue
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun OnboardingPage(
  page: OnboardingPageValue,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.padding(horizontal = 24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Image(
      painter = painterResource(page.imageRes),
      contentDescription = null,
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      contentScale = ContentScale.Fit,
    )

    Text(
      text = stringResource(page.titleRes),
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.headlineMedium,
    )

    Text(
      text = stringResource(page.descriptionRes),
      modifier = Modifier.padding(bottom = 8.dp),
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.bodyLarge,
    )
  }
}

@Preview
@Composable
private fun OnboardingPageDiscoverPreview() {
  DexReaderTheme {
    OnboardingPage(
      page = OnboardingPageValue.DISCOVER,
      modifier = Modifier.fillMaxSize(),
    )
  }
}

@Preview
@Composable
private fun OnboardingPageTrackPreview() {
  DexReaderTheme {
    OnboardingPage(
      page = OnboardingPageValue.TRACK,
      modifier = Modifier.fillMaxSize(),
    )
  }
}
