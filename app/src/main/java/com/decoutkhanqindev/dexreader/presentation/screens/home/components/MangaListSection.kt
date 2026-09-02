package com.decoutkhanqindev.dexreader.presentation.screens.home.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.decoutkhanqindev.dexreader.presentation.screens.common.TestTags
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.mapper.CriteriaMapper.toSortCriteriaValue
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.model.value.criteria.MangaSortCriteriaValue
import com.decoutkhanqindev.dexreader.presentation.model.value.language.LanguageValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaSectionValue
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.lists.manga.HorizontalMangaList
import com.decoutkhanqindev.dexreader.presentation.screens.common.sections.SectionHeader
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


@Composable
fun MangaListSection(
  section: MangaSectionValue,
  items: ImmutableList<MangaModel>,
  modifier: Modifier = Modifier,
  onItemClick: (String) -> Unit,
  onMoreClick: (sectionTitle: String, sortCriteria: MangaSortCriteriaValue) -> Unit,
) {
  val sectionTitle = stringResource(section.nameRes)

  Column(modifier = modifier) {
    SectionHeader(
      icon = section.icon,
      title = sectionTitle,
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          start = 16.dp,
          end = 16.dp,
          top = 8.dp,
          bottom = 4.dp
        ),
      onMoreClick = { onMoreClick(sectionTitle, section.toSortCriteriaValue()) }
    )
    HorizontalMangaList(
      items = items,
      modifier = Modifier
        .fillMaxWidth()
        .testTag(TestTags.HOME_SECTION_PREFIX + section.name),
      onItemClick = onItemClick,
    )
  }
}

private val previewMangaSection = persistentListOf(
  MangaModel(
    id = "1",
    title = "One Piece",
    coverUrl = "",
    description = "A pirate adventure.",
    author = "Eiichiro Oda",
    artist = "Eiichiro Oda",
    categories = persistentListOf(CategoryModel(id = "g1", title = "Action")),
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
    title = "Naruto",
    coverUrl = "",
    description = "A ninja story.",
    author = "Masashi Kishimoto",
    artist = "Masashi Kishimoto",
    categories = persistentListOf(CategoryModel(id = "g2", title = "Adventure")),
    status = MangaStatusValue.COMPLETED,
    contentRating = MangaContentRatingValue.SAFE,
    year = "1999",
    availableLanguages = persistentListOf(LanguageValue.ENGLISH),
    latestChapter = "700",
    updatedAt = "2014-11-10",
    rating = "8.7",
    follows = "1.8M",
  ),
  MangaModel(
    id = "3",
    title = "Attack on Titan",
    coverUrl = "",
    description = "Humanity vs Titans.",
    author = "Hajime Isayama",
    artist = "Hajime Isayama",
    categories = persistentListOf(CategoryModel(id = "g3", title = "Action")),
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
private fun MangaListSectionPreview() {
  DexReaderTheme {
    MangaListSection(
      section = MangaSectionValue.LATEST_UPDATE,
      items = previewMangaSection,
      modifier = Modifier.fillMaxWidth(),
      onItemClick = {},
      onMoreClick = { _, _ -> }
    )
  }
}