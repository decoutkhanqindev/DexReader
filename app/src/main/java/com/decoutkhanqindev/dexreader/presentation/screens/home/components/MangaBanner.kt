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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.MangaCoverArt
import com.decoutkhanqindev.dexreader.presentation.screens.common.onScalableClick
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmer
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
        .shimmer(isEnable = !isImageLoaded)
        .padding(horizontal = 8.dp)
        .clip(MaterialTheme.shapes.medium)
        .onScalableClick(MaterialTheme.shapes.medium) { onItemClick(manga.id) }
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
            topAlpha = 0.0f,
            bottomAlpha = 1f,
          ),
      )

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
        Button(
          onClick = { onItemClick(manga.id) },
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
          ),
          shape = MaterialTheme.shapes.medium
        ) {
          Text(text = stringResource(id = R.string.read_now), fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
