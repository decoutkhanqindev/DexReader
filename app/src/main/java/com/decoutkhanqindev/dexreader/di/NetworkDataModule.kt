package com.decoutkhanqindev.dexreader.di

import com.decoutkhanqindev.dexreader.BuildConfig
import com.decoutkhanqindev.dexreader.data.network.firebase.auth.FirebaseAuthSource
import com.decoutkhanqindev.dexreader.data.network.firebase.auth.FirebaseAuthSourceImpl
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.FirebaseFirestoreSource
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.FirebaseFirestoreSourceImpl
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.MangaDexApiService
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.interceptor.NetworkInterceptor
import com.decoutkhanqindev.dexreader.data.utils.IsoDateAdapter
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

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UsersCollectionQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FavoritesCollectionQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class HistoryCollectionQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CreatedAtFieldQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MangaIdFieldQualifier

@Module
@InstallIn(SingletonComponent::class)
object NetworkDataModule {
  @Provides
  @BaseUrlQualifier
  fun provideBaseUrl(): String = BuildConfig.BASE_URL

  @Provides
  @UploadUrlQualifier
  fun provideUploadUrl(): String = BuildConfig.UPLOAD_URL

  @Provides
  fun provideMoshi(): Moshi = Moshi.Builder()
    .add(IsoDateAdapter)
    .addLast(KotlinJsonAdapterFactory())
    .build()

  @Provides
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
  fun provideMangaDexApiService(
    @MangaDexApiServiceQualifier retrofit: Retrofit,
  ): MangaDexApiService = retrofit.create(MangaDexApiService::class.java)

  @Provides
  @UsersCollectionQualifier
  fun provideUsersCollection(): String = "users"

  @Provides
  @FavoritesCollectionQualifier
  fun provideFavoritesCollection(): String = "favorites"

  @Provides
  @HistoryCollectionQualifier
  fun provideHistoryCollection(): String = "history"

  @Provides
  @CreatedAtFieldQualifier
  fun provideCreatedAtField(): String = "createdAt"

  @Provides
  @MangaIdFieldQualifier
  fun provideMangaIdField(): String = "manga_id"

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
  fun provideFirebaseFirestoreSource(
    firebaseFirestoreSourceImpl: FirebaseFirestoreSourceImpl,
  ): FirebaseFirestoreSource = firebaseFirestoreSourceImpl
}
