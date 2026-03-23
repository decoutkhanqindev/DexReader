package com.decoutkhanqindev.dexreader.di

import com.decoutkhanqindev.dexreader.BuildConfig
import com.decoutkhanqindev.dexreader.data.network.adapter.IsoDateTimeAdapter
import com.decoutkhanqindev.dexreader.data.network.api.ApiService
import com.decoutkhanqindev.dexreader.data.network.firebase.auth.FirebaseAuthSource
import com.decoutkhanqindev.dexreader.data.network.firebase.auth.FirebaseAuthSourceImpl
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.favorite.FirebaseFavoriteFirestoreSource
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.favorite.FirebaseFavoriteFirestoreSourceImpl
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.history.FirebaseHistoryFirestoreSource
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.history.FirebaseHistoryFirestoreSourceImpl
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.user.FirebaseUserFirestoreSource
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.user.FirebaseUserFirestoreSourceImpl
import com.decoutkhanqindev.dexreader.data.network.interceptor.NetworkInterceptor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseUrlQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UploadUrlQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MangaDexApiServiceQualifier

@Module
@InstallIn(SingletonComponent::class)
object NetworkDataModule {
  @Provides
  @Singleton
  @BaseUrlQualifier
  fun provideBaseUrl(): String = BuildConfig.BASE_URL

  @Provides
  @Singleton
  @UploadUrlQualifier
  fun provideUploadUrl(): String = BuildConfig.UPLOAD_URL

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
  @MangaDexApiServiceQualifier
  fun provideRetrofit(
    @BaseUrlQualifier baseUrl: String,
    okHttpClient: OkHttpClient,
    moshi: Moshi,
  ): Retrofit =
    Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(okHttpClient)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()

  @Provides
  @Singleton
  fun provideMangaDexApiService(
    @MangaDexApiServiceQualifier retrofit: Retrofit,
  ): ApiService = retrofit.create(ApiService::class.java)

  @Provides
  @Singleton
  fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

  @Provides
  @Singleton
  fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

  @Provides
  @Singleton
  fun provideFirebaseAuthSource(
    firebaseAuthSourceImpl: FirebaseAuthSourceImpl,
  ): FirebaseAuthSource = firebaseAuthSourceImpl

  @Provides
  @Singleton
  fun provideFirebaseUserFirestoreSource(
    impl: FirebaseUserFirestoreSourceImpl,
  ): FirebaseUserFirestoreSource = impl

  @Provides
  @Singleton
  fun provideFirebaseFavoriteFirestoreSource(
    impl: FirebaseFavoriteFirestoreSourceImpl,
  ): FirebaseFavoriteFirestoreSource = impl

  @Provides
  @Singleton
  fun provideFirebaseHistoryFirestoreSource(
    impl: FirebaseHistoryFirestoreSourceImpl,
  ): FirebaseHistoryFirestoreSource = impl
}
