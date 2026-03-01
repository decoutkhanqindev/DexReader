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
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaContentRatingFilterOption
import com.decoutkhanqindev.dexreader.presentation.model.criteria.filter.MangaStatusFilterOption

@Composable
fun VerticalGridFilterCriteriaList(
  selectedStatusOptions: List<MangaStatusFilterOption>,
  onSelectedStatusOptions: (List<MangaStatusFilterOption>) -> Unit,
  selectedContentRatingOptions: List<MangaContentRatingFilterOption>,
  onSelectedContentRatingOptions: (List<MangaContentRatingFilterOption>) -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    modifier = modifier
  ) {
    item {
      FilterCriteriaItem(
        title = stringResource(R.string.filter_status),
        options = MangaStatusFilterOption.entries,
        selectedOptions = selectedStatusOptions,
        nameResOf = { it.nameRes },
        onSelectedOptions = onSelectedStatusOptions,
        modifier = Modifier
          .fillMaxWidth()
          .padding(4.dp)
      )
    }
    item {
      FilterCriteriaItem(
        title = stringResource(R.string.filter_content_rating),
        options = MangaContentRatingFilterOption.entries,
        selectedOptions = selectedContentRatingOptions,
        nameResOf = { it.nameRes },
        onSelectedOptions = onSelectedContentRatingOptions,
        modifier = Modifier
          .fillMaxWidth()
          .padding(4.dp)
      )
    }
  }
}
