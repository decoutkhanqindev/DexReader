package com.decoutkhanqindev.dexreader.di

import com.decoutkhanqindev.dexreader.data.repository.category.CategoryRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.manga.CacheRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.manga.ChapterRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.manga.MangaRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.manga.MangaStatsRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.network.NetworkRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.prefs.PrefsRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.user.FavoritesRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.user.HistoryRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.user.StatisticsRepositoryImpl
import com.decoutkhanqindev.dexreader.data.repository.user.UserRepositoryImpl
import com.decoutkhanqindev.dexreader.domain.repository.category.CategoryRepository
import com.decoutkhanqindev.dexreader.domain.repository.manga.CacheRepository
import com.decoutkhanqindev.dexreader.domain.repository.manga.ChapterRepository
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaRepository
import com.decoutkhanqindev.dexreader.domain.repository.manga.MangaStatsRepository
import com.decoutkhanqindev.dexreader.domain.repository.network.NetworkRepository
import com.decoutkhanqindev.dexreader.domain.repository.prefs.PrefsRepository
import com.decoutkhanqindev.dexreader.domain.repository.user.FavoritesRepository
import com.decoutkhanqindev.dexreader.domain.repository.user.HistoryRepository
import com.decoutkhanqindev.dexreader.domain.repository.user.StatisticsRepository
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
  fun bindStatisticsRepository(impl: StatisticsRepositoryImpl): StatisticsRepository

  @Binds
  @Singleton
  fun bindPrefsRepository(impl: PrefsRepositoryImpl): PrefsRepository

  @Binds
  @Singleton
  fun bindNetworkRepository(impl: NetworkRepositoryImpl): NetworkRepository

  @Binds
  @Singleton
  fun bindMangaStatsRepository(impl: MangaStatsRepositoryImpl): MangaStatsRepository
}
