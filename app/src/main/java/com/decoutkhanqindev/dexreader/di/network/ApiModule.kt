package com.decoutkhanqindev.dexreader.di.network

import com.decoutkhanqindev.dexreader.BuildConfig
import com.decoutkhanqindev.dexreader.data.network.adapter.IsoDateTimeAdapter
import com.decoutkhanqindev.dexreader.data.network.api.ApiService
import com.decoutkhanqindev.dexreader.data.network.interceptor.NetworkInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
  @Provides
  @Singleton
  fun provideMoshi(): Moshi = Moshi.Builder()
    .add(IsoDateTimeAdapter)
    .addLast(KotlinJsonAdapterFactory())
    .build()

  @Provides
  @Singleton
  fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
    HttpLoggingInterceptor().apply {
      level = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor.Level.BODY
      } else {
        HttpLoggingInterceptor.Level.NONE
      }
    }

  @Provides
  @Singleton
  fun provideOkHttpClient(
    authorizationInterceptor: NetworkInterceptor,
    httpLoggingInterceptor: HttpLoggingInterceptor,
  ): OkHttpClient =
    OkHttpClient.Builder()
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS)
      .addInterceptor(authorizationInterceptor)
      .addInterceptor(httpLoggingInterceptor)
      .build()

  @Provides
  @Singleton
  fun provideRetrofit(
    okHttpClient: OkHttpClient,
    moshi: Moshi,
  ): Retrofit =
    Retrofit.Builder()
      .baseUrl(BuildConfig.BASE_URL)
      .client(okHttpClient)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()

  @Provides
  @Singleton
  fun provideMangaDexApiService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)
}
