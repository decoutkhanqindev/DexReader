package com.decoutkhanqindev.dexreader.presentation.model.value.manga

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FiberNew
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.decoutkhanqindev.dexreader.R

@Immutable
enum class MangaSectionValue(
  @param:StringRes val nameRes: Int,
  val icon: ImageVector,
) {
  TRENDING(R.string.trending, Icons.AutoMirrored.Filled.TrendingUp),
  LATEST_UPDATE(R.string.latest_update, Icons.Default.Update),
  NEW_RELEASE(R.string.new_releases, Icons.Default.FiberNew),
  TOP_RATED(R.string.top_rated, Icons.Default.EmojiEvents)
}
