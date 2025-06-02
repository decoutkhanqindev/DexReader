package com.decoutkhanqindev.dexreader.di

import com.decoutkhanqindev.dexreader.presentation.ui.common.viewmodel.ChapterNavigationViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {
  @Provides
  @Singleton
  fun provideChapterNavigationViewModel(): ChapterNavigationViewModel {
    return ChapterNavigationViewModel()
  }
}