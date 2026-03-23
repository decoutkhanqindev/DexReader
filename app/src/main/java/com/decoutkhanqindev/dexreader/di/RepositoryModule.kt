package com.decoutkhanqindev.dexreader.di

import com.decoutkhanqindev.dexreader.data.repository.category.CategoryRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.manga.CacheRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.manga.ChapterRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.manga.MangaRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.settings.SettingsRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.user.FavoritesRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.user.HistoryRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.user.UserRepositoryImpl
import com.decoutkhanqindev.dexreader.domain.repository.category.CategoryRepository
import com.decoutkhanqindev.dexreader.domain.repository.manga.CacheRepository
import com.decoutkhanqindev.dexreader.domain.repository.manga.ChapterRepository
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaRepository
import com.decoutkhanqindev.dexreader.domain.repository.settings.SettingsRepository
import com.decoutkhanqindev.dexreader.domain.repository.user.FavoritesRepository
import com.decoutkhanqindev.dexreader.domain.repository.user.HistoryRepository
import com.decoutkhanqindev.dexreader.domain.repository.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
  @Binds
  @Singleton
  fun provideMangaRepository(repositoryImpl: MangaRepositoryImpl): MangaRepository

  @Binds
  @Singleton
  fun provideChapterRepository(impl: ChapterRepositoryImpl): ChapterRepository

  @Binds
  @Singleton
  fun provideCacheRepository(impl: CacheRepositoryImpl): CacheRepository

  @Binds
  @Singleton
  fun provideCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

  @Binds
  @Singleton
  fun provideUserRepository(impl: UserRepositoryImpl): UserRepository

  @Binds
  @Singleton
  fun provideFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository

  @Binds
  @Singleton
  fun provideHistoryRepository(impl: HistoryRepositoryImpl): HistoryRepository

  @Binds
  @Singleton
  fun provideSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
