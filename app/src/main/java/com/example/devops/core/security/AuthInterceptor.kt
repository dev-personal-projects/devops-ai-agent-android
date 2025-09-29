package com.example.devops.core.security

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Add mobile client identifier header
        val request = originalRequest.newBuilder()
            .header("X-Client-Type", "mobile")
            .header("User-Agent", "DevOpsApp-Android/1.0")
            .build()
        
        return chain.proceed(request)
    }
}