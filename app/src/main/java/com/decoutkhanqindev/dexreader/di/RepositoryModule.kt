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
  fun bindMangaRepository(impl: MangaRepositoryImpl): MangaRepository

  @Binds
  @Singleton
  fun bindChapterRepository(impl: ChapterRepositoryImpl): ChapterRepository

  @Binds
  @Singleton
  fun bindCacheRepository(impl: CacheRepositoryImpl): CacheRepository

  @Binds
  @Singleton
  fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

  @Binds
  @Singleton
  fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

  @Binds
  @Singleton
  fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository

  @Binds
  @Singleton
  fun bindHistoryRepository(impl: HistoryRepositoryImpl): HistoryRepository

  @Binds
  @Singleton
  fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
