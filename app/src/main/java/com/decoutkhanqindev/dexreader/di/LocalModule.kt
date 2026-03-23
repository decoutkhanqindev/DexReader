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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
  @Provides
  @Singleton
  fun provideChapterCacheDB(@ApplicationContext context: Context): ChapterCacheDatabase =
    Room.databaseBuilder(
      context = context,
      klass = ChapterCacheDatabase::class.java,
      name = ChapterCacheDatabase.CHAPTER_CACHE_DB_NAME
    )
      .fallbackToDestructiveMigration(true) // cache is re-fetchable on upgrade
      .build()

  @Provides
  @Singleton
  fun provideChapterCacheDao(db: ChapterCacheDatabase): ChapterCacheDao =
    db.chapterCacheDao()

  @Provides
  @Singleton
  fun provideThemePrefsManager(
    @ApplicationContext context: Context,
  ): DataStore<Preferences> = context.dataStore
}