package com.example.devops.di

import com.example.devops.ui.features.auth.data.remote.api.GitHubAuthApi
import com.example.devops.ui.features.auth.data.repository.AuthRepositoryImpl
import com.example.devops.ui.features.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideGitHubAuthApi(retrofit: Retrofit): GitHubAuthApi {
            return retrofit.create(GitHubAuthApi::class.java)
        }
    }
}