package com.decoutkhanqindev.dexreader.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
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

private const val CHAPTER_CACHE_DB_NAME = "chapter_cache.db"

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ThemeModeKeyQualifier

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {
  @Provides
  @Singleton
  fun provideChapterCacheDB(@ApplicationContext context: Context): ChapterCacheDatabase =
    Room.databaseBuilder(
      context = context.applicationContext,
      klass = ChapterCacheDatabase::class.java,
      name = CHAPTER_CACHE_DB_NAME
    )
      .fallbackToDestructiveMigration(true)
      .build()

  @Provides
  @Singleton
  fun provideChapterCacheDao(chapterCacheDatabase: ChapterCacheDatabase): ChapterCacheDao =
    chapterCacheDatabase.chapterCacheDao()

  @Provides
  @ThemeModeKeyQualifier
  fun provideThemeModeKey(): String = "theme_mode"

  @Provides
  @Singleton
  fun provideThemePrefsManager(
    @ApplicationContext context: Context,
  ): DataStore<Preferences> = context.dataStore
}