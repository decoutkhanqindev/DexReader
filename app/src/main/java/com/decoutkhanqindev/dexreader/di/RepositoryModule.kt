package com.decoutkhanqindev.dexreader.di

import com.decoutkhanqindev.dexreader.data.repository.manga.CacheRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.category.CategoryRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.manga.ChapterRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.user.FavoritesRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.user.HistoryRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.manga.MangaRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.settings.SettingsRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.user.UserRepositoryImpl
import com.decoutkhanqindev.dexreader.domain.repository.manga.CacheRepository
import com.decoutkhanqindev.dexreader.domain.repository.category.CategoryRepository
import com.decoutkhanqindev.dexreader.domain.repository.manga.ChapterRepository
import com.decoutkhanqindev.dexreader.domain.repository.user.FavoritesRepository
import com.decoutkhanqindev.dexreader.domain.repository.user.HistoryRepository
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaRepository
import com.decoutkhanqindev.dexreader.domain.repository.settings.SettingsRepository
import com.decoutkhanqindev.dexreader.domain.repository.user.UserRepository
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