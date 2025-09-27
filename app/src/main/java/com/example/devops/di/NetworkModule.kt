package com.example.devops.di

import com.example.devops.ui.features.auth.login.data.api.AuthApi
import com.example.devops.ui.features.auth.login.data.storage.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import com.example.devops.BuildConfig
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // This is a compile-time token used for @Named injection — MUST be a const val
    const val BASE_URL_NAMED = "BASE_URL"


    @Provides
    @Singleton
    @Named(BASE_URL_NAMED)
    fun provideBaseUrl(): String {
        val raw = BuildConfig.BACKEND_URL.trim()
        if (raw.isEmpty()) {
            throw IllegalStateException("BACKEND_URL is empty — check keys.properties / buildConfigField")
        }

        val httpUrl = raw.toHttpUrlOrNull()
            ?: throw IllegalStateException("Invalid BACKEND_URL in BuildConfig: $raw")

        // Ensure trailing slash for Retrofit expectation
        val normalized = if (httpUrl.toString().endsWith("/")) httpUrl.toString()
        else httpUrl.toString() + "/"
        return normalized
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @Named(BASE_URL_NAMED) baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}



@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = tokenManager.getAccessToken()

        return if (token != null) {
            val authenticatedRequest = request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(request)
        }
    }
}