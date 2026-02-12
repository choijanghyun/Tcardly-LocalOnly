package com.tcardly.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tcardly.data.repository.*
import com.tcardly.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds @Singleton
    abstract fun bindCardRepository(impl: CardRepositoryImpl): CardRepository

    @Binds @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds @Singleton
    abstract fun bindSubscriptionRepository(impl: SubscriptionRepositoryImpl): SubscriptionRepository

    @Binds @Singleton
    abstract fun bindCompanyRepository(impl: CompanyRepositoryImpl): CompanyRepository

    @Binds @Singleton
    abstract fun bindGroupTagRepository(impl: GroupTagRepositoryImpl): GroupTagRepository

    companion object {
        @Provides @Singleton
        fun provideGson(): Gson = GsonBuilder().create()
    }
}
