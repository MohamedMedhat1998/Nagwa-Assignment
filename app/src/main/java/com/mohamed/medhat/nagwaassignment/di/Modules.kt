package com.mohamed.medhat.nagwaassignment.di

import com.mohamed.medhat.nagwaassignment.networking.FakeApi
import com.mohamed.medhat.nagwaassignment.networking.NetworkApi
import com.mohamed.medhat.nagwaassignment.repository.FakeRepository
import com.mohamed.medhat.nagwaassignment.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

/**
 * Tells hilt how to bind dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class Modules {
    @Binds
    @FakeRepo
    abstract fun bindFakeRepository(fakeRepository: FakeRepository): Repository

    @Binds
    @FakeApiInstance
    abstract fun bindFakeApi(fakeApi: FakeApi): NetworkApi
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FakeRepo

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FakeApiInstance