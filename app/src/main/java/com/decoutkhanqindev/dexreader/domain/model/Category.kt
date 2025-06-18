package com.decoutkhanqindev.dexreader.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Category(
  val id: String,
  val title: String,
  val group: String,
)