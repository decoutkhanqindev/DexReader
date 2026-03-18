package com.decoutkhanqindev.dexreader.presentation.model.manga

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryUiModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class MangaUiModel(
  val id: String,
  val title: String,
  val coverUrl: String,
  val description: String,
  val author: String,
  val artist: String,
  val categories: ImmutableList<CategoryUiModel>,
  val status: MangaStatusUiModel,
  val contentRating: MangaContentRatingUiModel,
  val year: String,
  val availableLanguages: ImmutableList<MangaLanguageUiModel>,
  val latestChapter: String,
  val updatedAt: String,
)
