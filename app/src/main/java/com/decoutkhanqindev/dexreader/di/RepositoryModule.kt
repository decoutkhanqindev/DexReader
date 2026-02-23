package com.decoutkhanqindev.dexreader.di

import com.decoutkhanqindev.dexreader.data.repository.CacheRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.CategoryRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.ChapterRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.FavoritesRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.HistoryRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.MangaRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.SettingsRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.UserRepositoryImpl
import com.decoutkhanqindev.dexreader.domain.repository.CacheRepository
import com.decoutkhanqindev.dexreader.domain.repository.CategoryRepository
import com.decoutkhanqindev.dexreader.domain.repository.ChapterRepository
import com.decoutkhanqindev.dexreader.domain.repository.FavoritesRepository
import com.decoutkhanqindev.dexreader.domain.repository.HistoryRepository
import com.decoutkhanqindev.dexreader.domain.repository.MangaRepository
import com.decoutkhanqindev.dexreader.domain.repository.SettingsRepository
import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
  @Binds
  fun provideMangaRepository(repositoryImpl: MangaRepositoryImpl): MangaRepository

  @Binds
  fun provideChapterRepository(repositoryImpl: ChapterRepositoryImpl): ChapterRepository

  @Binds
  fun provideCacheRepository(repositoryImpl: CacheRepositoryImpl): CacheRepository

  @Binds
  fun provideCategoryRepository(repositoryImpl: CategoryRepositoryImpl): CategoryRepository

  @Binds
  fun provideUserRepository(repositoryImpl: UserRepositoryImpl): UserRepository

  @Binds
  fun provideFavoritesRepository(repositoryImpl: FavoritesRepositoryImpl): FavoritesRepository

  @Binds
  fun provideHistoryRepository(repositoryImpl: HistoryRepositoryImpl): HistoryRepository

  @Binds
  fun provideSettingsRepository(repositoryImpl: SettingsRepositoryImpl): SettingsRepository
}