package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.entity.manga.Manga
import com.decoutkhanqindev.dexreader.domain.value.manga.MangaContentRating
import com.decoutkhanqindev.dexreader.domain.value.manga.MangaStatus
import com.decoutkhanqindev.dexreader.presentation.mapper.CategoryMapper.toCategoryModel
import com.decoutkhanqindev.dexreader.presentation.mapper.LanguageMapper.toMangaLanguageEnum
import com.decoutkhanqindev.dexreader.presentation.model.manga.MangaModel
import com.decoutkhanqindev.dexreader.presentation.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.util.TimeAgo.toTimeAgo
import kotlinx.collections.immutable.toPersistentList

object MangaMapper {
  fun MangaStatus.toMangaStatusEnum() = MangaStatusValue.valueOf(this.name)
  fun MangaStatusValue.toMangaStatus() = MangaStatus.valueOf(this.name)
  fun MangaContentRating.toMangaContentRatingEnum() = MangaContentRatingValue.valueOf(this.name)
  fun MangaContentRatingValue.toMangaContentRating() = MangaContentRating.valueOf(this.name)

  fun Manga.toMangaModel() =
    MangaModel(
      id = id,
      title = title,
      coverUrl = coverUrl,
      description = description ?: Manga.DEFAULT_DESCRIPTION,
      author = author ?: Manga.DEFAULT_AUTHOR,
      artist = artist ?: Manga.DEFAULT_ARTIST,
      categories = categories.map { it.toCategoryModel() }.toPersistentList(),
      status = status.toMangaStatusEnum(),
      contentRating = contentRating.toMangaContentRatingEnum(),
      year = year ?: Manga.DEFAULT_YEAR,
      availableLanguages = availableLanguages.map { it.toMangaLanguageEnum() }
        .toPersistentList(),
      latestChapter = latestChapter ?: Manga.DEFAULT_LAST_CHAPTER,
      updatedAt = updatedAt.toTimeAgo(),
    )
}
