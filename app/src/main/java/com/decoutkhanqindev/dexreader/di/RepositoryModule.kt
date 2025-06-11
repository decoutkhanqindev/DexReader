package com.decoutkhanqindev.dexreader.di

import com.decoutkhanqindev.dexreader.data.repository.CacheRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.CategoryRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.ChapterRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.FavoritesRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.MangaRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.UserRepositoryImpl
import com.decoutkhanqindev.dexreader.domain.repository.CacheRepository
import com.decoutkhanqindev.dexreader.domain.repository.CategoryRepository
import com.decoutkhanqindev.dexreader.domain.repository.ChapterRepository
import com.decoutkhanqindev.dexreader.domain.repository.FavoritesRepository
import com.decoutkhanqindev.dexreader.domain.repository.MangaRepository
import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
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
  fun provideCacheRepository(
    cacheRepositoryImpl: CacheRepositoryImpl
  ): CacheRepository

  @Binds
  fun provideCategoryRepository(
    categoryRepositoryImpl: CategoryRepositoryImpl
  ): CategoryRepository

  @Binds
  fun provideUserRepository(
    userRepositoryImpl: UserRepositoryImpl
  ): UserRepository

  @Binds
  fun provideFavoritesRepository(
    favoritesRepositoryImpl: FavoritesRepositoryImpl
  ): FavoritesRepository
}