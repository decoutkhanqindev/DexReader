package com.decoutkhanqindev.dexreader.presentation.model.manga

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.value.manga.MangaContentRatingValue
import com.decoutkhanqindev.dexreader.presentation.value.manga.MangaLanguageValue
import com.decoutkhanqindev.dexreader.presentation.value.manga.MangaStatusValue
import com.decoutkhanqindev.dexreader.presentation.model.category.CategoryModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class MangaModel(
  val id: String,
  val title: String,
  val coverUrl: String,
  val description: String,
  val author: String,
  val artist: String,
  val categories: ImmutableList<CategoryModel>,
  val status: MangaStatusValue,
  val contentRating: MangaContentRatingValue,
  val year: String,
  val availableLanguages: ImmutableList<MangaLanguageValue>,
  val latestChapter: String,
  val updatedAt: String,
)
