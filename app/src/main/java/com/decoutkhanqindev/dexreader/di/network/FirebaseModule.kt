package com.decoutkhanqindev.dexreader.di.network

import com.decoutkhanqindev.dexreader.data.network.firebase.auth.FirebaseAuthSource
import com.decoutkhanqindev.dexreader.data.network.firebase.auth.FirebaseAuthSourceImpl
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.favorite.FirebaseFavoriteFirestoreSource
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.favorite.FirebaseFavoriteFirestoreSourceImpl
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.history.FirebaseHistoryFirestoreSource
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.history.FirebaseHistoryFirestoreSourceImpl
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.user.FirebaseUserFirestoreSource
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.user.FirebaseUserFirestoreSourceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
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
