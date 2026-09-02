package com.decoutkhanqindev.dexreader.presentation.model.value.language

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.util.LanguageManager
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
enum class LanguageValue(
  val code: String,
  val flag: String,
) {
  ENGLISH(code = "en", flag = "🇬🇧"),
  AFRIKAANS(code = "af", flag = "🇿🇦"),
  ALBANIAN(code = "sq", flag = "🇦🇱"),
  ARABIC(code = "ar", flag = "🇸🇦"),
  AZERBAIJANI(code = "az", flag = "🇦🇿"),
  BASQUE(code = "eu", flag = "🇪🇸"),
  BELARUSIAN(code = "be", flag = "🇧🇾"),
  BENGALI(code = "bn", flag = "🇧🇩"),
  BULGARIAN(code = "bg", flag = "🇧🇬"),
  BURMESE(code = "my", flag = "🇲🇲"),
  CATALAN(code = "ca", flag = "🇪🇸"),
  CHINESE_SIMPLIFIED(code = "zh", flag = "🇨🇳"),
  CHINESE_TRADITIONAL(code = "zh-hk", flag = "🇭🇰"),
  CHUVASH(code = "cv", flag = "🇷🇺"),
  CROATIAN(code = "hr", flag = "🇭🇷"),
  CZECH(code = "cs", flag = "🇨🇿"),
  DANISH(code = "da", flag = "🇩🇰"),
  DUTCH(code = "nl", flag = "🇳🇱"),
  ESPERANTO(code = "eo", flag = "🏳️"),
  ESTONIAN(code = "et", flag = "🇪🇪"),
  FILIPINO(code = "fil", flag = "🇵🇭"),
  FINNISH(code = "fi", flag = "🇫🇮"),
  FRENCH(code = "fr", flag = "🇫🇷"),
  GEORGIAN(code = "ka", flag = "🇬🇪"),
  GERMAN(code = "de", flag = "🇩🇪"),
  GREEK(code = "el", flag = "🇬🇷"),
  HEBREW(code = "he", flag = "🇮🇱"),
  HINDI(code = "hi", flag = "🇮🇳"),
  HUNGARIAN(code = "hu", flag = "🇭🇺"),
  INDONESIAN(code = "id", flag = "🇮🇩"),
  IRISH(code = "ga", flag = "🇮🇪"),
  ITALIAN(code = "it", flag = "🇮🇹"),
  JAPANESE(code = "ja", flag = "🇯🇵"),
  JAVANESE(code = "jv", flag = "🇮🇩"),
  KAZAKH(code = "kk", flag = "🇰🇿"),
  KOREAN(code = "ko", flag = "🇰🇷"),
  LATIN(code = "la", flag = "🇻🇦"),
  LATVIAN(code = "lv", flag = "🇱🇻"),
  LITHUANIAN(code = "lt", flag = "🇱🇹"),
  MALAY(code = "ms", flag = "🇲🇾"),
  MONGOLIAN(code = "mn", flag = "🇲🇳"),
  NEPALI(code = "ne", flag = "🇳🇵"),
  NORWEGIAN(code = "no", flag = "🇳🇴"),
  PERSIAN(code = "fa", flag = "🇮🇷"),
  POLISH(code = "pl", flag = "🇵🇱"),
  PORTUGUESE(code = "pt", flag = "🇵🇹"),
  PORTUGUESE_BR(code = "pt-br", flag = "🇧🇷"),
  ROMANIAN(code = "ro", flag = "🇷🇴"),
  RUSSIAN(code = "ru", flag = "🇷🇺"),
  SERBIAN(code = "sr", flag = "🇷🇸"),
  SLOVAK(code = "sk", flag = "🇸🇰"),
  SLOVENIAN(code = "sl", flag = "🇸🇮"),
  SPANISH(code = "es", flag = "🇪🇸"),
  SPANISH_LATAM(code = "es-la", flag = "🏳️"),
  SWEDISH(code = "sv", flag = "🇸🇪"),
  TAMIL(code = "ta", flag = "🇮🇳"),
  TELUGU(code = "te", flag = "🇮🇳"),
  THAI(code = "th", flag = "🇹🇭"),
  TAGALOG(code = "tl", flag = "🇵🇭"),
  TURKISH(code = "tr", flag = "🇹🇷"),
  UKRAINIAN(code = "uk", flag = "🇺🇦"),
  URDU(code = "ur", flag = "🇵🇰"),
  UZBEK(code = "uz", flag = "🇺🇿"),
  VIETNAMESE(code = "vi", flag = "🇻🇳");

  companion object {
    val DEFAULT = ENGLISH

    fun fromCode(code: String?): LanguageValue =
      entries.find { it.code.equals(other = code, ignoreCase = true) } ?: DEFAULT

    fun sortedForDisplay(
      deviceLanguageCode: String,
      displayIn: LanguageValue,
    ): ImmutableList<LanguageValue> {
      val deviceLanguage = entries.find { it.code.equals(deviceLanguageCode, ignoreCase = true) }

      return entries.sortedWith(
        compareBy<LanguageValue> {
          when (it) {
            deviceLanguage -> 0
            ENGLISH -> 1
            else -> 2
          }
        }.thenBy { LanguageManager.displayNameOf(code = it.code, displayIn = displayIn) }
      ).toImmutableList()
    }
  }
}