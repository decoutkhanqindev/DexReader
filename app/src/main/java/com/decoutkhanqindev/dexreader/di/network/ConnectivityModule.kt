package com.decoutkhanqindev.dexreader.di.network

import com.decoutkhanqindev.dexreader.data.network.connectivity.NetworkManager
import com.decoutkhanqindev.dexreader.data.network.connectivity.NetworkManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule {
  @Provides
  @Singleton
  fun provideNetworkManager(impl: NetworkManagerImpl): NetworkManager = impl
}
