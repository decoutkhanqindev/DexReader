package com.decoutkhanqindev.dexreader.presentation.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.MangaCoverArt
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmerLoading
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay

@Composable
fun MangaBanner(
  mangaList: ImmutableList<MangaModel>,
  modifier: Modifier = Modifier,
  onItemClick: (String) -> Unit,
) {
  val pagerState = rememberPagerState(pageCount = { mangaList.size })

  LaunchedEffect(Unit) {
    while (true) {
      delay(3000)
      val nextPage = (pagerState.currentPage + 1) % mangaList.size
      pagerState.animateScrollToPage(nextPage)
    }
  }

  HorizontalPager(
    state = pagerState,
    modifier = modifier
  ) { page ->
    val manga = mangaList[page]
    var isImageLoaded by remember(manga.id) { mutableStateOf(false) }

    Box(
      modifier = Modifier
        .fillMaxSize()
        .shimmerLoading(shape = MaterialTheme.shapes.medium, isEnable = !isImageLoaded)
        .padding(horizontal = 8.dp)
        .onClick(MaterialTheme.shapes.medium) { onItemClick(manga.id) }
    ) {
      MangaCoverArt(
        url = manga.coverUrl,
        title = manga.title,
        modifier = Modifier.fillMaxSize(),
        onImageLoaded = { isImageLoaded = true }
      )

      // Gradient Overlay
      Box(
        modifier = Modifier
          .fillMaxSize()
          .blurBackground(
            color = Color.Black,
            topAlpha = 0f,
            topCenterAlpha = 0.1f,
            bottomCenterAlpha = 0.8f,
            bottomAlpha = 1f,
            startY = 350f,
          )
      )

      // Status Badge
      Surface(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(8.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
        shape = MaterialTheme.shapes.small,
        tonalElevation = 4.dp
      ) {
        Text(
          text = stringResource(manga.status.nameRes).uppercase(),
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Black,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )
      }

      Column(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = manga.title,
          style = MaterialTheme.typography.headlineLarge,
          color = Color.White,
          fontWeight = FontWeight.ExtraBold,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = manga.description,
          style = MaterialTheme.typography.bodyMedium,
          color = Color.White.copy(alpha = 0.7f),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
        ActionButton(
          isHighlighted = true,
          backgroundColor = MaterialTheme.colorScheme.primary,
          onClick = remember(manga.id) { { onItemClick(manga.id) } }
        ) {
          Text(
            text = stringResource(id = R.string.read_now),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelLarge,
          )
        }
      }
    }
  }
}
