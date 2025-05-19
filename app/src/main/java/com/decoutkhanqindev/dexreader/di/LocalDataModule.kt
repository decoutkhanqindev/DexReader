package com.decoutkhanqindev.dexreader.di

import android.content.Context
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
object LocalDataModule {
  @Provides
  @Singleton
  fun provideChapterCacheDB(@ApplicationContext context: Context): ChapterCacheDatabase =
    ChapterCacheDatabase.getInstance(context)

  @Provides
  @Singleton
  fun provideChapterCacheDao(chapterCacheDatabase: ChapterCacheDatabase): ChapterCacheDao =
    chapterCacheDatabase.chapterCacheDao()
}