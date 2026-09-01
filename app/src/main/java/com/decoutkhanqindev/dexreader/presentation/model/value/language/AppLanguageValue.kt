package com.decoutkhanqindev.dexreader.presentation.model.value.language

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.value.manga.MangaLanguageValue
import com.decoutkhanqindev.dexreader.util.LanguageManager
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
enum class AppLanguageValue(val language: MangaLanguageValue) {
  ENGLISH(MangaLanguageValue.ENGLISH),
  AFRIKAANS(MangaLanguageValue.AFRIKAANS),
  ALBANIAN(MangaLanguageValue.ALBANIAN),
  ARABIC(MangaLanguageValue.ARABIC),
  AZERBAIJANI(MangaLanguageValue.AZERBAIJANI),
  BASQUE(MangaLanguageValue.BASQUE),
  BELARUSIAN(MangaLanguageValue.BELARUSIAN),
  BENGALI(MangaLanguageValue.BENGALI),
  BULGARIAN(MangaLanguageValue.BULGARIAN),
  BURMESE(MangaLanguageValue.BURMESE),
  CATALAN(MangaLanguageValue.CATALAN),
  CHINESE_SIMPLIFIED(MangaLanguageValue.CHINESE_SIMPLIFIED),
  CHINESE_TRADITIONAL(MangaLanguageValue.CHINESE_TRADITIONAL),
  CHUVASH(MangaLanguageValue.CHUVASH),
  CROATIAN(MangaLanguageValue.CROATIAN),
  CZECH(MangaLanguageValue.CZECH),
  DANISH(MangaLanguageValue.DANISH),
  DUTCH(MangaLanguageValue.DUTCH),
  ESPERANTO(MangaLanguageValue.ESPERANTO),
  ESTONIAN(MangaLanguageValue.ESTONIAN),
  FILIPINO(MangaLanguageValue.FILIPINO),
  FINNISH(MangaLanguageValue.FINNISH),
  FRENCH(MangaLanguageValue.FRENCH),
  GEORGIAN(MangaLanguageValue.GEORGIAN),
  GERMAN(MangaLanguageValue.GERMAN),
  GREEK(MangaLanguageValue.GREEK),
  HEBREW(MangaLanguageValue.HEBREW),
  HINDI(MangaLanguageValue.HINDI),
  HUNGARIAN(MangaLanguageValue.HUNGARIAN),
  INDONESIAN(MangaLanguageValue.INDONESIAN),
  IRISH(MangaLanguageValue.IRISH),
  ITALIAN(MangaLanguageValue.ITALIAN),
  JAPANESE(MangaLanguageValue.JAPANESE),
  JAVANESE(MangaLanguageValue.JAVANESE),
  KAZAKH(MangaLanguageValue.KAZAKH),
  KOREAN(MangaLanguageValue.KOREAN),
  LATIN(MangaLanguageValue.LATIN),
  LATVIAN(MangaLanguageValue.LATVIAN),
  LITHUANIAN(MangaLanguageValue.LITHUANIAN),
  MALAY(MangaLanguageValue.MALAY),
  MONGOLIAN(MangaLanguageValue.MONGOLIAN),
  NEPALI(MangaLanguageValue.NEPALI),
  NORWEGIAN(MangaLanguageValue.NORWEGIAN),
  PERSIAN(MangaLanguageValue.PERSIAN),
  POLISH(MangaLanguageValue.POLISH),
  PORTUGUESE(MangaLanguageValue.PORTUGUESE),
  PORTUGUESE_BR(MangaLanguageValue.PORTUGUESE_BR),
  ROMANIAN(MangaLanguageValue.ROMANIAN),
  RUSSIAN(MangaLanguageValue.RUSSIAN),
  SERBIAN(MangaLanguageValue.SERBIAN),
  SLOVAK(MangaLanguageValue.SLOVAK),
  SLOVENIAN(MangaLanguageValue.SLOVENIAN),
  SPANISH(MangaLanguageValue.SPANISH),
  SPANISH_LATAM(MangaLanguageValue.SPANISH_LATAM),
  SWEDISH(MangaLanguageValue.SWEDISH),
  TAMIL(MangaLanguageValue.TAMIL),
  TELUGU(MangaLanguageValue.TELUGU),
  THAI(MangaLanguageValue.THAI),
  TAGALOG(MangaLanguageValue.TAGALOG),
  TURKISH(MangaLanguageValue.TURKISH),
  UKRAINIAN(MangaLanguageValue.UKRAINIAN),
  URDU(MangaLanguageValue.URDU),
  UZBEK(MangaLanguageValue.UZBEK),
  VIETNAMESE(MangaLanguageValue.VIETNAMESE);

  val code: String get() = language.code
  val flag: String get() = language.flag

  companion object {
    val DEFAULT = ENGLISH

    fun fromCode(code: String?): AppLanguageValue =
      entries.find { it.code.equals(other = code, ignoreCase = true) } ?: DEFAULT

    fun sortedForDisplay(
      deviceLanguageCode: String,
      displayIn: AppLanguageValue,
    ): ImmutableList<AppLanguageValue> {
      val deviceLanguage = entries.find { it.code.equals(deviceLanguageCode, ignoreCase = true) }

      return entries
        .sortedWith(
          compareBy<AppLanguageValue> {
            when (it) {
              deviceLanguage -> 0
              ENGLISH -> 1
              else -> 2
            }
          }.thenBy { LanguageManager.displayNameOf(code = it.code, displayIn = displayIn) }
        )
        .toImmutableList()
    }
  }
}
