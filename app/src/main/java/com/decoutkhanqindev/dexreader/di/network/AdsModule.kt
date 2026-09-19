package com.decoutkhanqindev.dexreader.di.network

import android.app.Application
import android.content.Context
import com.decoutkhanqindev.dexreader.ads.AdsManager
import com.decoutkhanqindev.dexreader.data.network.connectivity.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdsModule {

  @Provides
  @Singleton
  fun provideAdsManager(
    application: Application,
    networkManager: NetworkManager,
  ): AdsManager = AdsManager(application, networkManager)
}