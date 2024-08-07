package com.example.workouttracker.modules.network.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Response

// TODO move this API key somewhere safer

class ApiNinjaHeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.run {
            proceed(
                request()
                    .newBuilder()
                    .addHeader("X-Api-Key", "wiv9ydSUSqf0LeT1QJuJjA==Yr0FGDNfvrchJGep")
                    .build()
            )
        }
    }
}