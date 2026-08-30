package com.decoutkhanqindev.dexreader.presentation.screens.categories.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.screens.categories.CategoryCoverUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.animateItemOnAppear
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.MangaCoverArt
import com.decoutkhanqindev.dexreader.presentation.screens.common.onClick
import com.decoutkhanqindev.dexreader.presentation.screens.common.shimmerLoading
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import com.decoutkhanqindev.dexreader.presentation.theme.OnScrim

@Composable
fun CategoryCard(
  category: CategoryModel,
  coverState: CategoryCoverUiState,
  modifier: Modifier = Modifier,
  onCategoryClick: (categoryId: String, title: String, description: String) -> Unit,
  onLoadCover: (String) -> Unit,
) {
  SideEffect(category.id) { onLoadCover(category.id) }

  val coverUrl = (coverState as? CategoryCoverUiState.Success)?.coverUrl.orEmpty()
  var isImageLoaded by remember(coverUrl) { mutableStateOf(false) }
  val showShimmer = coverState is CategoryCoverUiState.Loading || !isImageLoaded
  val shape = MaterialTheme.shapes.medium

  Card(
    modifier = modifier
      .animateItemOnAppear()
      .onClick(shape = shape) { onCategoryClick(category.id, category.title, category.description) },
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    shape = shape,
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    )
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      MangaCoverArt(
        url = coverUrl,
        modifier = Modifier
          .fillMaxSize()
          .shimmerLoading(
            shape = shape,
            isEnable = showShimmer
          ),
        onImageLoaded = { isImageLoaded = true }
      )

      Box(
        modifier = Modifier
          .fillMaxSize()
          .blurBackground(
            color = MaterialTheme.colorScheme.scrim,
            topAlpha = 0f,
            topCenterAlpha = 0.1f,
            bottomCenterAlpha = 0.8f,
            bottomAlpha = 1f,
            startY = 350f,
          )
      )

      Column(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
      ) {
        Text(
          text = category.title,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = OnScrim,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = category.description,
          style = MaterialTheme.typography.labelMedium,
          color = OnScrim.copy(alpha = 0.8f),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun CategoryCardLoadingPreview() {
  DexReaderTheme {
    CategoryCard(
      category = CategoryModel(id = "1", title = "Action"),
      coverState = CategoryCoverUiState.Loading,
      modifier = Modifier
        .fillMaxWidth(0.5f)
        .height(250.dp),
      onCategoryClick = { _, _, _ -> },
      onLoadCover = {},
    )
  }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun CategoryCardFallbackPreview() {
  DexReaderTheme {
    CategoryCard(
      category = CategoryModel(id = "2", title = "Slice of Life"),
      coverState = CategoryCoverUiState.Success(coverUrl = ""),
      modifier = Modifier
        .fillMaxWidth(0.5f)
        .height(250.dp),
      onCategoryClick = { _, _, _ -> },
      onLoadCover = {},
    )
  }
}
