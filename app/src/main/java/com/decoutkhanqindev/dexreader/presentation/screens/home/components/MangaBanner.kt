package com.decoutkhanqindev.dexreader.presentation.screens.home.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
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
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaLanguageValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.MangaCoverArt
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmerLoading
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import com.decoutkhanqindev.dexreader.presentation.theme.OnScrim
import com.decoutkhanqindev.dexreader.presentation.theme.RatingStarGold
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
        .shimmerLoading(shape = MaterialTheme.shapes.medium, isEnable = !isImageLoaded)
        .onClick(MaterialTheme.shapes.medium) { onItemClick(manga.id) }
    ) {
      MangaCoverArt(
        url = manga.coverUrl,
        title = manga.title,
        modifier = Modifier.fillMaxSize(),
        onImageLoaded = { isImageLoaded = true }
      )

      // Top Scrim
      Box(
        modifier = Modifier
          .fillMaxSize()
          .blurBackground(
            color = MaterialTheme.colorScheme.scrim,
            topAlpha = 0.5f,
            bottomAlpha = 0f,
            endY = 320f,
          )
      )

      // Bottom Scrim
      Box(
        modifier = Modifier
          .fillMaxSize()
          .blurBackground(
            color = MaterialTheme.colorScheme.scrim,
            topAlpha = 0f,
            topCenterAlpha = 0.4f,
            bottomCenterAlpha = 0.8f,
            bottomAlpha = 1f,
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
          Surface(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
            shape = MaterialTheme.shapes.small,
            shadowElevation = 4.dp
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = RatingStarGold,
                modifier = Modifier.size(12.dp)
              )
              Text(
                text = manga.rating,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }
          }

          Surface(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
            shape = MaterialTheme.shapes.small,
            tonalElevation = 4.dp
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = manga.status.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(12.dp)
              )
              Text(
                text = stringResource(manga.status.nameRes).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }
          }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (manga.categories.isNotEmpty()) {
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            manga.categories.take(2).forEach { category ->
              GenreTag(label = category.title)
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
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
          maxLines = 1,
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
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelLarge,
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

@Composable
private fun GenreTag(label: String, modifier: Modifier = Modifier) {
  Surface(
    modifier = modifier,
    color = OnScrim.copy(alpha = 0.16f),
    contentColor = OnScrim,
    shape = MaterialTheme.shapes.small,
    border = BorderStroke(1.dp, OnScrim.copy(alpha = 0.35f))
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      fontWeight = FontWeight.SemiBold,
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
    )
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
    availableLanguages = persistentListOf(MangaLanguageValue.ENGLISH),
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
    availableLanguages = persistentListOf(MangaLanguageValue.ENGLISH),
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
