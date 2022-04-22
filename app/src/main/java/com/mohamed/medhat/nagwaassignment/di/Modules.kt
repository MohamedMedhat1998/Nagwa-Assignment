package com.mohamed.medhat.nagwaassignment.di

import android.content.Context
import androidx.work.WorkManager
import com.mohamed.medhat.nagwaassignment.networking.FakeApi
import com.mohamed.medhat.nagwaassignment.networking.NetworkApi
import com.mohamed.medhat.nagwaassignment.repository.FakeRepository
import com.mohamed.medhat.nagwaassignment.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

/**
 * Tells hilt how to bind dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class Binders {
    @Binds
    @FakeRepo
    abstract fun bindFakeRepository(fakeRepository: FakeRepository): Repository

    @Binds
    @FakeApiInstance
    abstract fun bindFakeApi(fakeApi: FakeApi): NetworkApi
}

@Module
@InstallIn(SingletonComponent::class)
object Providers {
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FakeRepo

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FakeApiInstance