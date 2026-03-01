package com.decoutkhanqindev.dexreader.util

import com.decoutkhanqindev.dexreader.domain.model.MangaLanguage

/**
 * Language codec utility - bidirectional encoding/decoding between language names and domain types.
 * Display names are a presentation concern and live here.
 */
object LanguageCodec {

  /**
   * Converts a language code to its full language name
   * Example: "en" -> "English", "ja" -> "Japanese"
   */
  fun String?.toFullLanguageName(): String {
    val languageMap = mapOf(
      "en" to "English",
      "af" to "Afrikaans",
      "sq" to "Albanian",
      "ar" to "Arabic",
      "az" to "Azerbaijani",
      "eu" to "Basque",
      "be" to "Belarusian",
      "bn" to "Bengali",
      "bg" to "Bulgarian",
      "my" to "Burmese",
      "ca" to "Catalan",
      "zh" to "Chinese (Simplified)",
      "zh-hk" to "Chinese (Traditional)",
      "cv" to "Chuvash",
      "hr" to "Croatian",
      "cs" to "Czech",
      "da" to "Danish",
      "nl" to "Dutch",
      "eo" to "Esperanto",
      "et" to "Estonian",
      "fil" to "Filipino",
      "fi" to "Finnish",
      "fr" to "French",
      "ka" to "Georgian",
      "de" to "German",
      "el" to "Greek",
      "he" to "Hebrew",
      "hi" to "Hindi",
      "hu" to "Hungarian",
      "id" to "Indonesian",
      "ga" to "Irish",
      "it" to "Italian",
      "ja" to "Japanese",
      "jv" to "Javanese",
      "kk" to "Kazakh",
      "ko" to "Korean",
      "la" to "Latin",
      "lt" to "Lithuanian",
      "ms" to "Malay",
      "mn" to "Mongolian",
      "ne" to "Nepali",
      "no" to "Norwegian",
      "fa" to "Persian",
      "pl" to "Polish",
      "pt-br" to "Portuguese (BR)",
      "ro" to "Romanian",
      "ru" to "Russian",
      "sr" to "Serbian",
      "sk" to "Slovak",
      "sl" to "Slovenian",
      "es-la" to "Spanish (LATAM)",
      "sv" to "Swedish",
      "ta" to "Tamil",
      "te" to "Telugu",
      "th" to "Thai",
      "tr" to "Turkish",
      "uk" to "Ukrainian",
      "ur" to "Urdu",
      "uz" to "Uzbek",
      "vi" to "Vietnamese",
      "es" to "Spanish (LATAM)",
      "tl" to "Thailand"
    )
    return languageMap[this] ?: this ?: "Unknown"
  }

  /**
   * Converts a full language name to its language code
   * Example: "English" -> "en", "Japanese" -> "ja"
   */
  fun String?.toLanguageCode(): String {
    val languageMap = mapOf(
      "English" to "en",
      "Afrikaans" to "af",
      "Albanian" to "sq",
      "Arabic" to "ar",
      "Azerbaijani" to "az",
      "Basque" to "eu",
      "Belarusian" to "be",
      "Bengali" to "bn",
      "Bulgarian" to "bg",
      "Burmese" to "my",
      "Catalan" to "ca",
      "Chinese (Simplified)" to "zh",
      "Chinese (Traditional)" to "zh-hk",
      "Chuvash" to "cv",
      "Croatian" to "hr",
      "Czech" to "cs",
      "Danish" to "da",
      "Dutch" to "nl",
      "Esperanto" to "eo",
      "Estonian" to "et",
      "Filipino" to "fil",
      "Finnish" to "fi",
      "French" to "fr",
      "Georgian" to "ka",
      "German" to "de",
      "Greek" to "el",
      "Hebrew" to "he",
      "Hindi" to "hi",
      "Hungarian" to "hu",
      "Indonesian" to "id",
      "Irish" to "ga",
      "Italian" to "it",
      "Japanese" to "ja",
      "Javanese" to "jv",
      "Kazakh" to "kk",
      "Korean" to "ko",
      "Latin" to "la",
      "Lithuanian" to "lt",
      "Malay" to "ms",
      "Mongolian" to "mn",
      "Nepali" to "ne",
      "Norwegian" to "no",
      "Persian" to "fa",
      "Polish" to "pl",
      "Portuguese (BR)" to "pt-br",
      "Romanian" to "ro",
      "Russian" to "ru",
      "Serbian" to "sr",
      "Slovak" to "sk",
      "Slovenian" to "sl",
      "Spanish (LATAM)" to "es-la",
      "Swedish" to "sv",
      "Tamil" to "ta",
      "Telugu" to "te",
      "Thai" to "th",
      "Turkish" to "tr",
      "Ukrainian" to "uk",
      "Urdu" to "ur",
      "Uzbek" to "uz",
      "Vietnamese" to "vi",
      "Spanish (LATAM)" to "es",
      "Thailand" to "tl"
    )
    return languageMap[this] ?: this ?: "en"
  }

  /**
   * Maps a MangaLanguage domain enum to its display name for the UI.
   * Display names are a presentation concern and belong here, not in the domain model.
   */
  fun MangaLanguage.toDisplayName(): String = when (this) {
    MangaLanguage.ENGLISH -> "English"
    MangaLanguage.AFRIKAANS -> "Afrikaans"
    MangaLanguage.ALBANIAN -> "Albanian"
    MangaLanguage.ARABIC -> "Arabic"
    MangaLanguage.AZERBAIJANI -> "Azerbaijani"
    MangaLanguage.BASQUE -> "Basque"
    MangaLanguage.BELARUSIAN -> "Belarusian"
    MangaLanguage.BENGALI -> "Bengali"
    MangaLanguage.BULGARIAN -> "Bulgarian"
    MangaLanguage.BURMESE -> "Burmese"
    MangaLanguage.CATALAN -> "Catalan"
    MangaLanguage.CHINESE_SIMPLIFIED -> "Chinese (Simplified)"
    MangaLanguage.CHINESE_TRADITIONAL -> "Chinese (Traditional)"
    MangaLanguage.CHUVASH -> "Chuvash"
    MangaLanguage.CROATIAN -> "Croatian"
    MangaLanguage.CZECH -> "Czech"
    MangaLanguage.DANISH -> "Danish"
    MangaLanguage.DUTCH -> "Dutch"
    MangaLanguage.ESPERANTO -> "Esperanto"
    MangaLanguage.ESTONIAN -> "Estonian"
    MangaLanguage.FILIPINO -> "Filipino"
    MangaLanguage.FINNISH -> "Finnish"
    MangaLanguage.FRENCH -> "French"
    MangaLanguage.GEORGIAN -> "Georgian"
    MangaLanguage.GERMAN -> "German"
    MangaLanguage.GREEK -> "Greek"
    MangaLanguage.HEBREW -> "Hebrew"
    MangaLanguage.HINDI -> "Hindi"
    MangaLanguage.HUNGARIAN -> "Hungarian"
    MangaLanguage.INDONESIAN -> "Indonesian"
    MangaLanguage.IRISH -> "Irish"
    MangaLanguage.ITALIAN -> "Italian"
    MangaLanguage.JAPANESE -> "Japanese"
    MangaLanguage.JAVANESE -> "Javanese"
    MangaLanguage.KAZAKH -> "Kazakh"
    MangaLanguage.KOREAN -> "Korean"
    MangaLanguage.LATIN -> "Latin"
    MangaLanguage.LITHUANIAN -> "Lithuanian"
    MangaLanguage.MALAY -> "Malay"
    MangaLanguage.MONGOLIAN -> "Mongolian"
    MangaLanguage.NEPALI -> "Nepali"
    MangaLanguage.NORWEGIAN -> "Norwegian"
    MangaLanguage.PERSIAN -> "Persian"
    MangaLanguage.POLISH -> "Polish"
    MangaLanguage.PORTUGUESE_BR -> "Portuguese (BR)"
    MangaLanguage.ROMANIAN -> "Romanian"
    MangaLanguage.RUSSIAN -> "Russian"
    MangaLanguage.SERBIAN -> "Serbian"
    MangaLanguage.SLOVAK -> "Slovak"
    MangaLanguage.SLOVENIAN -> "Slovenian"
    MangaLanguage.SPANISH -> "Spanish (LATAM)"
    MangaLanguage.SPANISH_LATAM -> "Spanish (LATAM)"
    MangaLanguage.SWEDISH -> "Swedish"
    MangaLanguage.TAMIL -> "Tamil"
    MangaLanguage.TELUGU -> "Telugu"
    MangaLanguage.THAI -> "Thai"
    MangaLanguage.THAILAND -> "Thailand"
    MangaLanguage.TURKISH -> "Turkish"
    MangaLanguage.UKRAINIAN -> "Ukrainian"
    MangaLanguage.URDU -> "Urdu"
    MangaLanguage.UZBEK -> "Uzbek"
    MangaLanguage.VIETNAMESE -> "Vietnamese"
    MangaLanguage.UNKNOWN -> "Unknown"
  }

  /**
   * Converts a display name (from the UI language selector) back to a MangaLanguage domain enum.
   */
  fun String.toMangaLanguageByName(): MangaLanguage =
    MangaLanguage.entries.find { it.toDisplayName() == this } ?: MangaLanguage.UNKNOWN
}
