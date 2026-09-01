package com.decoutkhanqindev.dexreader.data.mapper

object LocalizedTextMapper {
  const val FALLBACK_LANGUAGE_CODE = "en"

  fun Map<String, String>?.localized(
    languageCode: String,
    altTexts: List<Map<String, String>>? = null,
  ): String? =
    this?.get(languageCode)
      ?: altTexts?.firstNotNullOfOrNull { it[languageCode] }
      ?: this?.get(FALLBACK_LANGUAGE_CODE)
      ?: altTexts?.firstNotNullOfOrNull { it[FALLBACK_LANGUAGE_CODE] }
      ?: this?.values?.firstOrNull()
}
