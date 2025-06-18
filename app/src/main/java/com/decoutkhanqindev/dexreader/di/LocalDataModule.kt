package com.decoutkhanqindev.dexreader.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.decoutkhanqindev.dexreader.data.local.database.ChapterCacheDatabase
import com.decoutkhanqindev.dexreader.data.local.database.dao.ChapterCacheDao
import com.decoutkhanqindev.dexreader.data.local.prefs.ThemePrefsManager.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IsDynamicThemeKeyQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ThemeTypeKeyQualifier

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {
  @Provides
  @Singleton
  fun provideChapterCacheDB(@ApplicationContext context: Context): ChapterCacheDatabase =
    ChapterCacheDatabase.getInstance(context)

  @Provides
  @Singleton
  fun provideChapterCacheDao(chapterCacheDatabase: ChapterCacheDatabase): ChapterCacheDao =
    chapterCacheDatabase.chapterCacheDao()

  @Provides
  @ThemeTypeKeyQualifier
  fun provideThemeTypeKey(): String = "theme_type"

  @Provides
  @Singleton
  fun provideThemePrefsManager(
    @ApplicationContext context: Context
  ): DataStore<Preferences> = context.dataStore
}