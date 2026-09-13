package com.decoutkhanqindev.dexreader.data.local.locale

import android.app.Application
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale
import javax.inject.Inject

class LanguageManagerImpl @Inject constructor(
  private val app: Application,
) : LanguageManager {
  override fun deviceLanguageCode(): String =
    Resources.getSystem().configuration.locales[0].language

  override fun configurationFor(languageCode: String): Configuration {
    val locale = Locale.forLanguageTag(languageCode)
    Locale.setDefault(locale)
    return Configuration(app.resources.configuration).apply {
      setLocale(locale)
      setLayoutDirection(locale)
    }
  }

  override fun resourcesFor(configuration: Configuration): Resources =
    app.createConfigurationContext(configuration).resources

  override fun displayNameOf(code: String, displayIn: String): String {
    val locale = Locale.forLanguageTag(code)
    val displayLocale = Locale.forLanguageTag(displayIn)
    val name =
      if (locale.country.isNotEmpty()) locale.getDisplayName(displayLocale)
      else locale.getDisplayLanguage(displayLocale)
    return name.replaceFirstChar { it.uppercase(displayLocale) }
  }
}
