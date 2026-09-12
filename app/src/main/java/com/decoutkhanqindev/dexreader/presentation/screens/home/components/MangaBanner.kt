package com.decoutkhanqindev.dexreader.presentation.screens.home.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.badges.MangaGenreChip
import com.decoutkhanqindev.dexreader.presentation.screens.common.badges.MangaRatingChip
import com.decoutkhanqindev.dexreader.presentation.screens.common.badges.MangaStatusBadge
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.MangaCoverArt
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmerLoading
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import com.decoutkhanqindev.dexreader.presentation.theme.OnScrim
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@Composable
fun MangaBanner(
  items: ImmutableList<MangaModel>,
  modifier: Modifier = Modifier,
  onItemClick: (String) -> Unit,
) {
  val pagerState = rememberPagerState(pageCount = { items.size })
  val autoScrollDurationMillis = 3000
  val autoScrollProgress = remember { Animatable(0f) }

  LaunchedEffect(Unit) {
    while (true) {
      delay(autoScrollDurationMillis.toLong())
      val nextPage = (pagerState.currentPage + 1) % items.size
      pagerState.animateScrollToPage(nextPage)
    }
  }

  LaunchedEffect(pagerState.currentPage) {
    autoScrollProgress.snapTo(0f)
    autoScrollProgress.animateTo(
      targetValue = 1f,
      animationSpec = tween(durationMillis = autoScrollDurationMillis, easing = LinearEasing)
    )
  }

  HorizontalPager(
    state = pagerState,
    beyondViewportPageCount = 2,
    modifier = modifier
  ) { page ->
    val manga = items[page]
    var isImageLoaded by remember(manga.id) { mutableStateOf(false) }

    Box(
      modifier = Modifier
        .graphicsLayer {
          val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
            .absoluteValue
            .coerceIn(0f, 1f)
          val scale = lerp(0.92f, 1f, 1f - pageOffset)
          scaleX = scale
          scaleY = scale
          alpha = lerp(0.6f, 1f, 1f - pageOffset)
        }
        .fillMaxSize()
        .padding(horizontal = 8.dp)
        .onClick(MaterialTheme.shapes.medium) { onItemClick(manga.id) }
    ) {
      MangaCoverArt(
        url = manga.coverUrl,
        modifier = Modifier
          .fillMaxSize()
          .shimmerLoading(
            shape = MaterialTheme.shapes.medium,
            isEnable = !isImageLoaded
          ),
        onImageLoaded = { isImageLoaded = true }
      )

      // Top Scrim
      Box(
        modifier = Modifier
          .fillMaxSize()
          .blurBackground(
            alphas = persistentListOf(0.5f, 0.5f, 0f, 0f),
            color = MaterialTheme.colorScheme.scrim,
            endY = 320f,
          )
      )

      // Bottom Scrim
      Box(
        modifier = Modifier
          .fillMaxSize()
          .blurBackground(
            alphas = persistentListOf(0f, 0.4f, 0.8f, 1f),
            color = MaterialTheme.colorScheme.scrim,
            startY = 350f,
          )
      )

      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(12.dp)
      ) {
        AutoScrollProgressIndicator(
          pageCount = items.size,
          currentPage = pagerState.currentPage,
          progress = { autoScrollProgress.value },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          MangaRatingChip(rating = manga.rating)
          MangaStatusBadge(status = manga.status)
        }

        Spacer(modifier = Modifier.weight(1f))

        if (manga.categories.isNotEmpty()) {
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            manga.categories.take(2).forEach { category ->
              MangaGenreChip(label = category.title)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
          text = manga.title,
          style = MaterialTheme.typography.headlineLarge.copy(
            shadow = Shadow(
              color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.6f),
              offset = Offset(0f, 2f),
              blurRadius = 8f
            )
          ),
          color = OnScrim,
          fontWeight = FontWeight.ExtraBold,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = manga.description,
          style = MaterialTheme.typography.bodyMedium,
          color = OnScrim.copy(alpha = 0.7f),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        ActionButton(
          isHighlighted = true,
          backgroundColor = MaterialTheme.colorScheme.primary,
          modifier = Modifier.fillMaxWidth(),
          onClick = remember(manga.id) { { onItemClick(manga.id) } }
        ) {
          Text(
            text = stringResource(id = R.string.read_now),
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
          )
          Spacer(modifier = Modifier.width(8.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun AutoScrollProgressIndicator(
  pageCount: Int,
  currentPage: Int,
  progress: () -> Float,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    repeat(pageCount) { index ->
      Box(
        modifier = Modifier
          .weight(1f)
          .height(3.dp)
          .clip(CircleShape)
          .background(OnScrim.copy(alpha = 0.35f))
          .drawWithContent {
            drawContent()
            val fraction = when {
              index < currentPage -> 1f
              index == currentPage -> progress()
              else -> 0f
            }
            drawRect(color = OnScrim, size = size.copy(width = size.width * fraction))
          }
      )
    }
  }
}

private val previewMangaList = persistentListOf(
  MangaModel(
    id = "1",
    title = "One Piece",
    coverUrl = "",
    description = "Follow Monkey D. Luffy on his quest to become the Pirate King and find the legendary One Piece treasure.",
    author = "Eiichiro Oda",
    artist = "Eiichiro Oda",
    categories = persistentListOf(
      CategoryModel(id = "g1", title = "Action"),
      CategoryModel(id = "g2", title = "Adventure"),
    ),
    status = MangaStatusValue.ON_GOING,
    contentRating = MangaContentRatingValue.SAFE,
    year = "1997",
    availableLanguages = persistentListOf(LanguageValue.ENGLISH),
    latestChapter = "1110",
    updatedAt = "2024-01-01",
    rating = "9.1",
    follows = "2.3M",
  ),
  MangaModel(
    id = "2",
    title = "Attack on Titan",
    coverUrl = "",
    description = "Humanity fights for survival against the Titans from behind the walls.",
    author = "Hajime Isayama",
    artist = "Hajime Isayama",
    categories = persistentListOf(CategoryModel(id = "g3", title = "Drama")),
    status = MangaStatusValue.COMPLETED,
    contentRating = MangaContentRatingValue.SAFE,
    year = "2009",
    availableLanguages = persistentListOf(LanguageValue.ENGLISH),
    latestChapter = "139",
    updatedAt = "2021-04-09",
    rating = "9.0",
    follows = "1.2M",
  ),
)

@Preview
@Composable
private fun MangaBannerPreview() {
  DexReaderTheme {
    MangaBanner(
      items = previewMangaList,
      modifier = Modifier
        .fillMaxWidth()
        .height(320.dp),
      onItemClick = {}
    )
  }
}
