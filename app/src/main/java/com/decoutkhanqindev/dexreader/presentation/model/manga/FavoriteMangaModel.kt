package com.decoutkhanqindev.dexreader.presentation.model.manga

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaStatusValue

@Immutable
data class FavoriteMangaModel(
  val id: String,
  val title: String,
  val coverUrl: String,
  val author: String,
  val status: MangaStatusValue,
)
