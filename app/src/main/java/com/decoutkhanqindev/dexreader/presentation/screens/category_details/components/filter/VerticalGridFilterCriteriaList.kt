package com.decoutkhanqindev.dexreader.presentation.screens.category_details.components.filter


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun VerticalGridFilterCriteriaList(
  selectedStatusOptions: ImmutableList<MangaStatusValue>,
  onStatusOptionsSelect: (ImmutableList<MangaStatusValue>) -> Unit,
  selectedContentRatingOptions: ImmutableList<MangaContentRatingValue>,
  onContentRatingOptionsSelect: (ImmutableList<MangaContentRatingValue>) -> Unit,
  modifier: Modifier = Modifier,
) {
  val statusItems = remember {
    MangaStatusValue.entries.filter { it != MangaStatusValue.UNKNOWN }.toPersistentList()
  }
  val contentRatingItems = remember {
    MangaContentRatingValue.entries.filter { it != MangaContentRatingValue.UNKNOWN }
      .toPersistentList()
  }

  Row(modifier = modifier) {
    FilterCriteriaItem(
      title = stringResource(R.string.filter_status),
      items = statusItems,
      selectedItems = selectedStatusOptions,
      nameResOf = { it.nameRes },
      onItemsSelect = onStatusOptionsSelect,
      modifier = Modifier
        .weight(1f)
        .padding(4.dp)
    )
    FilterCriteriaItem(
      title = stringResource(R.string.filter_content_rating),
      items = contentRatingItems,
      selectedItems = selectedContentRatingOptions,
      nameResOf = { it.nameRes },
      onItemsSelect = onContentRatingOptionsSelect,
      modifier = Modifier
        .weight(1f)
        .padding(4.dp)
    )
  }
}

@Preview
@Composable
private fun VerticalGridFilterCriteriaListPreview() {
  DexReaderTheme {
    VerticalGridFilterCriteriaList(
      selectedStatusOptions = persistentListOf(MangaStatusValue.ON_GOING),
      onStatusOptionsSelect = {},
      selectedContentRatingOptions = persistentListOf(MangaContentRatingValue.SAFE),
      onContentRatingOptionsSelect = {},
      modifier = Modifier
        .fillMaxWidth()
        .height(400.dp)
    )
  }
}
