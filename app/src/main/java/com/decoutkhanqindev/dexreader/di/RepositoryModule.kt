package com.decoutkhanqindev.dexreader.di

import com.decoutkhanqindev.dexreader.data.repository.CacheRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.ChapterRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.MangaRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.TagRepositoryImpl
import com.decoutkhanqindev.dexreader.domain.repository.CacheRepository
import com.decoutkhanqindev.dexreader.domain.repository.ChapterRepository
import com.decoutkhanqindev.dexreader.domain.repository.MangaRepository
import com.decoutkhanqindev.dexreader.domain.repository.TagRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
  @Binds
  fun provideMangaRepository(
    mangaRepositoryImpl: MangaRepositoryImpl
  ): MangaRepository

  @Binds
  fun provideChapterRepository(
    chapterRepositoryImpl: ChapterRepositoryImpl
  ): ChapterRepository

  @Binds
  fun provideCacheRepository (
    cacheRepositoryImpl: CacheRepositoryImpl
  ): CacheRepository

  @Binds
  fun provideTagRepository(
    categoryRepositoryImpl: TagRepositoryImpl
  ): TagRepository
}