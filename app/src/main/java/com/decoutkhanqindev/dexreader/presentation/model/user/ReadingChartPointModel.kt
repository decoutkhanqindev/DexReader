package com.decoutkhanqindev.dexreader.presentation.model.user

import androidx.compose.runtime.Immutable

@Immutable
data class ReadingChartPointModel(
  val id: String,
  val label: String,
  val minutes: Int,
)
