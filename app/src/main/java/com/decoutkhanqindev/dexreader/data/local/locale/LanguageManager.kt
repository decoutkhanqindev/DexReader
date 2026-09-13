package com.decoutkhanqindev.dexreader.data.local.locale

import android.content.res.Configuration
import android.content.res.Resources

interface LanguageManager {
  fun deviceLanguageCode(): String
  fun configurationFor(languageCode: String): Configuration
  fun resourcesFor(configuration: Configuration): Resources
  fun displayNameOf(code: String, displayIn: String): String
}
