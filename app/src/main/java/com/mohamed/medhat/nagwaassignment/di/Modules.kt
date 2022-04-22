package com.mohamed.medhat.nagwaassignment.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.work.WorkManager
import com.mohamed.medhat.nagwaassignment.networking.FakeApi
import com.mohamed.medhat.nagwaassignment.networking.NetworkApi
import com.mohamed.medhat.nagwaassignment.repository.FakeRepository
import com.mohamed.medhat.nagwaassignment.repository.Repository
import com.mohamed.medhat.nagwaassignment.utils.state.DataItemStateKeeper
import com.mohamed.medhat.nagwaassignment.utils.state.PrefsDataItemStateKeeper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Tells hilt how to bind instances.
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

    @Binds
    @Singleton
    @PrefsStateKeeper
    abstract fun bindPrefsStateKeeper(prefsDataItemStateKeeper: PrefsDataItemStateKeeper): DataItemStateKeeper
}

/**
 * Tells hilt how to provide instances.
 */
@Module
@InstallIn(SingletonComponent::class)
object Providers {
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FakeRepo

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FakeApiInstance

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PrefsStateKeeper