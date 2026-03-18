package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.model.manga.Manga
import com.decoutkhanqindev.dexreader.domain.model.manga.MangaContentRating
import com.decoutkhanqindev.dexreader.domain.model.manga.MangaStatus
import com.decoutkhanqindev.dexreader.presentation.mapper.CategoryUiMapper.toCategoryUiModel
import com.decoutkhanqindev.dexreader.presentation.mapper.LanguageMapper.toMangaLanguageUiModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaContentRatingUiModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaStatusUiModel
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaUiModel
import com.decoutkhanqindev.dexreader.util.TimeAgo.toTimeAgo
import kotlinx.collections.immutable.toPersistentList

object MangaUiMapper {
  fun MangaStatus.toMangaStatusUiModel() = MangaStatusUiModel.valueOf(this.name)
  fun MangaStatusUiModel.toMangaStatus() = MangaStatus.valueOf(this.name)
  fun MangaContentRating.toMangaContentRatingUiModel() = MangaContentRatingUiModel.valueOf(this.name)
  fun MangaContentRatingUiModel.toMangaContentRating() = MangaContentRating.valueOf(this.name)

  fun Manga.toMangaUiModel() =
    MangaUiModel(
      id = id,
      title = title,
      coverUrl = coverUrl,
      description = description ?: Manga.DEFAULT_DESCRIPTION,
      author = author ?: Manga.DEFAULT_AUTHOR,
      artist = artist ?: Manga.DEFAULT_ARTIST,
      categories = categories.map { it.toCategoryUiModel() }.toPersistentList(),
      status = status.toMangaStatusUiModel(),
      contentRating = contentRating.toMangaContentRatingUiModel(),
      year = year ?: Manga.DEFAULT_YEAR,
      availableLanguages = availableLanguages.map { it.toMangaLanguageUiModel() }
        .toPersistentList(),
      latestChapter = latestChapter ?: Manga.DEFAULT_LAST_CHAPTER,
      updatedAt = updatedAt.toTimeAgo(),
    )
}
