package com.decoutkhanqindev.dexreader.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.dexreader.R

private val JsFont = FontFamily(
  Font(R.font.js_regular, FontWeight.Normal),
  Font(R.font.js_medium, FontWeight.Medium),
  Font(R.font.js_semibold, FontWeight.SemiBold),
  Font(R.font.js_bold, FontWeight.Bold),
  Font(R.font.js_extrabold, FontWeight.ExtraBold),
)

val Typography = Typography(
  // Body text
  bodySmall = TextStyle(
    fontFamily = JsFont,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.4.sp
  ),
  bodyMedium = TextStyle(
    fontFamily = JsFont,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.25.sp
  ),
  bodyLarge = TextStyle(
    fontFamily = JsFont,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp
  ),

  // Label text
  labelSmall = TextStyle(
    fontFamily = JsFont,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
  ),
  labelMedium = TextStyle(
    fontFamily = JsFont,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
  ),
  labelLarge = TextStyle(
    fontFamily = JsFont,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp
  ),

  // Title text
  titleSmall = TextStyle(
    fontFamily = JsFont,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp
  ),
  titleMedium = TextStyle(
    fontFamily = JsFont,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.15.sp
  ),
  titleLarge = TextStyle(
    fontFamily = JsFont,
    fontWeight = FontWeight.Bold,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp
  )
)
