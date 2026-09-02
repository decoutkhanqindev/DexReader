package com.decoutkhanqindev.dexreader.di

import android.content.Context
import androidx.room.Room
import com.decoutkhanqindev.dexreader.data.local.database.ChapterCacheDatabase
import com.decoutkhanqindev.dexreader.data.local.database.dao.ChapterCacheDao
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
}