package com.example.updateraho.di

import androidx.transition.Visibility.Mode
import com.example.updateraho.firebase.FirebaseCommon
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import org.checkerframework.checker.units.qual.s
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestoreDatabase() = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseCommon(
        firestore: FirebaseFirestore,
        firebaseAuth : FirebaseAuth
    ) = FirebaseCommon(firestore,firebaseAuth)
}