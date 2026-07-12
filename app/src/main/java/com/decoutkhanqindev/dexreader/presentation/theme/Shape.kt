package com.decoutkhanqindev.dexreader.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes by lazy {
  Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
  )
}

val TopCardShape by lazy { RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp) }
val BottomCardShape by lazy { RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp) }

