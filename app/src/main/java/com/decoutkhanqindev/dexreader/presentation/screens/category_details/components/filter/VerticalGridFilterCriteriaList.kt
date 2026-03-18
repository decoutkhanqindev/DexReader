package com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.filter


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaContentRatingModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaStatusModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun VerticalGridFilterCriteriaList(
  selectedStatusOptions: ImmutableList<MangaStatusModel>,
  onStatusOptionsSelect: (ImmutableList<MangaStatusModel>) -> Unit,
  selectedContentRatingOptions: ImmutableList<MangaContentRatingModel>,
  onContentRatingOptionsSelect: (ImmutableList<MangaContentRatingModel>) -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    modifier = modifier
  ) {
    item {
      FilterCriteriaItem(
        title = stringResource(R.string.filter_status),
        items = MangaStatusModel.entries.filter {
          it != MangaStatusModel.UNKNOWN
        }.toPersistentList(),
        selectedItems = selectedStatusOptions,
        nameResOf = { it.nameRes },
        onItemsSelect = onStatusOptionsSelect,
        modifier = Modifier
          .fillMaxWidth()
          .padding(4.dp)
      )
    }
    item {
      FilterCriteriaItem(
        title = stringResource(R.string.filter_content_rating),
        items = MangaContentRatingModel.entries.filter {
          it != MangaContentRatingModel.UNKNOWN
        }.toPersistentList(),
        selectedItems = selectedContentRatingOptions,
        nameResOf = { it.nameRes },
        onItemsSelect = onContentRatingOptionsSelect,
        modifier = Modifier
          .fillMaxWidth()
          .padding(4.dp)
      )
    }
  }
}
