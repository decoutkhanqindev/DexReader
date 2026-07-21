package com.decoutkhanqindev.dexreader.presentation.model.value.manga

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.decoutkhanqindev.dexreader.R

@Immutable
enum class MangaStatusValue(
  @param:StringRes val nameRes: Int,
  val icon: ImageVector,
) {
  ON_GOING(R.string.status_on_going, Icons.Default.Autorenew),
  COMPLETED(R.string.status_completed, Icons.Default.CheckCircle),
  HIATUS(R.string.status_hiatus, Icons.Default.PauseCircle),
  CANCELLED(R.string.status_cancelled, Icons.Default.Cancel),
  UNKNOWN(R.string.status_unknown, Icons.AutoMirrored.Filled.HelpOutline)
}
