package com.decoutkhanqindev.dexreader.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import com.decoutkhanqindev.dexreader.presentation.model.value.language.AppLanguageValue
import java.util.Locale

object LanguageManager {
  val LocalAppLanguage = staticCompositionLocalOf { AppLanguageValue.DEFAULT }

  val current: AppLanguageValue
    @Composable
    @ReadOnlyComposable
    get() = LocalAppLanguage.current

  fun localeOf(code: String): Locale = Locale.forLanguageTag(code)

  fun deviceLanguageCode(): String =
    Resources.getSystem().configuration.locales[0].language

  fun configurationFor(context: Context, language: AppLanguageValue): Configuration {
    val locale = localeOf(language.code)
    Locale.setDefault(locale)
    return Configuration(context.resources.configuration).apply {
      setLocale(locale)
      setLayoutDirection(locale)
    }
  }

  fun displayNameOf(code: String, displayIn: AppLanguageValue): String {
    val locale = localeOf(code)
    val name =
      if (locale.country.isNotEmpty()) locale.getDisplayName(localeOf(displayIn.code))
      else locale.getDisplayLanguage(localeOf(displayIn.code))
    return name.replaceFirstChar { it.uppercase(localeOf(displayIn.code)) }
  }

  @Composable
  fun labelOf(code: String, flag: String): String = "$flag  ${displayNameOf(code, current)}"

  @Composable
  fun ProvideAppLanguage(
    language: AppLanguageValue,
    content: @Composable () -> Unit,
  ) {
    val context = LocalContext.current
    val configuration = remember(language, context) { configurationFor(context, language) }
    val resources = remember(configuration) {
      context.createConfigurationContext(configuration).resources
    }

    CompositionLocalProvider(
      LocalAppLanguage provides language,
      LocalConfiguration provides configuration,
      LocalResources provides resources,
      content = content,
    )
  }
}
